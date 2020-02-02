package com.alan.app.mvvm.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import com.facebook.binaryresource.FileBinaryResource
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory
import com.facebook.imagepipeline.core.ImagePipelineFactory
import com.facebook.imagepipeline.request.ImageRequest
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

object ImageUtil {

    enum class ScalingLogic {
        CROP, FIT
    }

    fun resizePhoto(srcFile: File, dstFile: File, dstWidth: Int, dstHeight: Int): Boolean {
        val bitmap = decodeFile(srcFile.path, dstWidth, dstHeight, ScalingLogic.FIT)
        return saveToFile(bitmap, dstFile)
    }

    fun decodeFile(path: String, dstWidth: Int, dstHeight: Int, scalingLogic: ScalingLogic): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        options.inJustDecodeBounds = false
        options.inSampleSize =
                calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight)

        return BitmapFactory.decodeFile(path, options)
    }

    /**
     * Calculate optimal down-sampling factor given the dimensions of a source
     * image, the dimensions of a destination area and a scaling logic.
     *
     * @param srcWidth Width of source image
     * @param srcHeight Height of source image
     * @param dstWidth Width of destination area
     * @param dstHeight Height of destination area
     * @return Optimal down scaling sample size for decoding
     */
    fun calculateSampleSize(srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int): Int {

        if (srcWidth < dstWidth && srcHeight < dstHeight) return 1

        val widthScale = Math.round(srcWidth.toFloat() / dstWidth.toFloat())
        val heightScale = Math.round(srcHeight.toFloat() / dstHeight.toFloat())

        return if (widthScale > heightScale)
            widthScale
        else
            heightScale
    }

    /**
     * Calculate optimal down-sampling factor given the dimensions of a source
     * image, the dimensions of a destination area and a scaling logic.
     *
     * @param srcWidth Width of source image
     * @param srcHeight Height of source image
     * @param dstWidth Width of destination area
     * @param dstHeight Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Optimal down scaling sample size for decoding
     */
    fun calculateSampleSize(srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int, scalingLogic: ScalingLogic): Int {

        if (srcWidth < dstWidth && srcHeight < dstHeight) return 1

        if (scalingLogic == ScalingLogic.FIT) {
            val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
            val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

            return if (srcAspect > dstAspect) {
                srcWidth / dstWidth
            } else {
                srcHeight / dstHeight
            }
        } else {
            val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
            val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

            return if (srcAspect > dstAspect) {
                srcHeight / dstHeight
            } else {
                srcWidth / dstWidth
            }
        }
    }

    fun saveToFile(bitmap: Bitmap, dstFile: File): Boolean {
        try {
            val fos = FileOutputStream(dstFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
            fos.flush()
            fos.close()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

    }

    fun saveBitmap(file: File, bitmap: Bitmap): Boolean {
        var outStream: OutputStream? = null
        try {
            outStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            outStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (outStream != null) {
                try {
                    outStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            bitmap.recycle()
        }
        return file.exists()
    }

    fun fixOrientationBugOfProcessedBitmap(file: File, context: Context): Bitmap? {
        try {
            val bitmap = BitmapFactory.decodeFile(file.path)
            return if (getCameraPhotoOrientation(context, Uri.parse(file.path)) == 0) {
                bitmap
            } else {
                val matrix = Matrix()
                matrix.postRotate(
                        getCameraPhotoOrientation(
                                context,
                                Uri.parse(file.path)
                        ).toFloat()
                )
                // recreate the new Bitmap and set it back
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
    }

    fun getCameraPhotoOrientation(context: Context, imageUri: Uri): Int {
        var rotate = 0
        try {
            context.contentResolver.notifyChange(imageUri, null)
            val exif = ExifInterface(imageUri.path ?: "")
            val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return rotate
    }

    fun generateTimeStampPhotoFile(context: Context, mediaDirName: String): File {
        val outputDir = context.filesDir
        val mediaDir = File(outputDir, mediaDirName)
        if (!mediaDir.exists()) {
            mediaDir.mkdirs()
        }
        return File(mediaDir, "media-${System.currentTimeMillis()}.jpg")
    }

    fun processBeforeUpload(context: Context?, file: File, bitmap: Bitmap, dir: String): File? {
        context ?: return null
        saveBitmap(file, bitmap)
        val resizedFile = generateTimeStampPhotoFile(context, dir)
        resizePhoto(file, resizedFile, 1024, 1024)
        return resizedFile
    }

    fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        val downloadRequest = ImageRequest.fromUri(uri)

        val cacheKey =
                DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(downloadRequest, context)

        if (ImagePipelineFactory.getInstance().mainFileCache.hasKey(cacheKey)) {
            val resource = ImagePipelineFactory.getInstance().mainFileCache.getResource(cacheKey)
            val cachedFile = (resource as FileBinaryResource).file
            return fixOrientationBugOfProcessedBitmap(cachedFile, context)
        }
        return null
    }
}