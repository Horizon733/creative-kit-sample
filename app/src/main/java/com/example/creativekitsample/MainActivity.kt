package com.example.creativekitsample

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.creativekitsample.ui.theme.CreativekitSampleTheme
import com.example.creativekitsample.ui.theme.SnapbtnColor
import com.snapchat.kit.sdk.SnapCreative
import com.snapchat.kit.sdk.creative.media.SnapLensLaunchData
import com.snapchat.kit.sdk.creative.media.SnapMediaFactory
import com.snapchat.kit.sdk.creative.media.SnapSticker
import com.snapchat.kit.sdk.creative.models.SnapPhotoContent
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import com.snapchat.kit.sdk.creative.models.SnapLensContent

private lateinit var snapFile: File
private lateinit var stickerFile: File
private val SNAP_NAME = "snap"
private val STICKER_NAME = "sticker"

class MainActivity : ComponentActivity() {

    @ExperimentalCoilApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        snapFile  = File(cacheDir, SNAP_NAME)
        stickerFile  = File(cacheDir, STICKER_NAME)
        setContent {
            CreativekitSampleTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val snapCreativeKitApi = SnapCreative.getApi(context)
    val snapMediaFactory = SnapCreative.getMediaFactory(context)
    val imageBitmap = remember { mutableStateOf<Bitmap?>(null) }
    val contentResolver = LocalContext.current.contentResolver
    val isStickerEnabled = remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){
        val inputStream = contentResolver.openInputStream(it)!!
        imageBitmap.value = MediaStore.Images.Media.getBitmap(contentResolver, it)
        copyFile(inputStream, snapFile)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imageBitmap.value != null) {
            Image(
                painter = rememberImagePainter(
                    data = imageBitmap.value
                ),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Button(
            onClick = { launcher.launch("image/*") },
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(backgroundColor = SnapbtnColor),
            enabled = true
        ) {
            Text(
                "Open Image"
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Add sticker")
            Switch(
                checked = isStickerEnabled.value,
                onCheckedChange = { isStickerEnabled.value = it }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (imageBitmap.value != null) {
                    val content =
                        SnapPhotoContent(snapMediaFactory.getSnapPhotoFromFile(snapFile))
                    if(isStickerEnabled.value){
                        if(!stickerFile.exists()){
                            val stickerUri = Uri.parse("android.resource://com.example.creativekitsample/drawable/sticker")
                            val inputStream = contentResolver.openInputStream(stickerUri)
                            copyFile(inputStream!!, stickerFile)
                        }

                        content.snapSticker = addSticker(snapMediaFactory = snapMediaFactory)
                    }
                    snapCreativeKitApi.send(content)
                } else {
                    Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                }
            },
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(backgroundColor = SnapbtnColor),
            enabled = true
        ) {
            Text(
                "Share on Snapchat"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                snapCreativeKitApi.send(getLens())
            },
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(backgroundColor = SnapbtnColor),
            enabled = true
        ) {
            Text(
                "Check out our on Snapchat!"
            )
        }
    }
}

fun addSticker( snapMediaFactory: SnapMediaFactory): SnapSticker{
    val snapSticker = snapMediaFactory.getSnapStickerFromFile(stickerFile)
    snapSticker.setHeightDp(100F)
    snapSticker.setWidthDp(100F)
    snapSticker.setPosX(0.2f)
    snapSticker.setPosY(0.8f)
    snapSticker.setRotationDegreesClockwise(45.0f)
    return snapSticker
}

fun getLens(): SnapLensContent {
    val launchData = SnapLensLaunchData.Builder()
        .build()
    val snapLensContent = SnapLensContent.createSnapLensContent(
        "your lens uuid",
        launchData
    )
    snapLensContent.attachmentUrl = "https://snapchat.com"
    return snapLensContent

}

fun copyFile(inputStream: InputStream, file: File){
    val buffer = ByteArray(1024)
    var length: Int
    FileOutputStream(file).use { outputStream ->
        while (inputStream.read(buffer).also {
                length = it
            } != -1) {
            outputStream.write(buffer, 0, length)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CreativekitSampleTheme {
        MainScreen()
    }
}



