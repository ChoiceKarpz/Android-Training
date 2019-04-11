package au.com.gridstone.trainingkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import com.squareup.picasso.Picasso

private const val KEY_IMAGE_ID = "image_id"

class DetailsController(args: Bundle) : Controller(args) {

  constructor(id: String) : this(bundleOf(KEY_IMAGE_ID to id))

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup
  ): View {
    val view = inflater.inflate(R.layout.controller_details, container, false)
    APIManager.getCachedImageForID(args.getString(KEY_IMAGE_ID))
        ?.let { imageData ->
          view.findViewById<TextView>(R.id.imageDetailsTitleTextView)
              .text = imageData.id
          view.findViewById<View>(R.id.detailTextOverlayView)
              .background.mutate()
              .alpha = 200
          Picasso.get()
              .load(imageData.imageUrl)
              .into(view.findViewById<ImageView>(R.id.imageDetailsImageView))
          view.findViewById<TextView>(R.id.imageDetailsTitleTextView)
              .text = imageData.title
          view.findViewById<TextView>(R.id.imageWidthValueTextView)
              .text = "${imageData.width} px"
          view.findViewById<TextView>(R.id.imageHeightValueTextView)
              .text = "${imageData.height} px"
          view.findViewById<TextView>(R.id.viewCountValueTextView)
              .text = imageData.views
              .toString()
          imageData.dateTime
              ?.let { imageTimeInSeconds ->
                val currentTimeInSeconds = System.currentTimeMillis() / 1000
                val seconds = currentTimeInSeconds - imageTimeInSeconds
                val minutes = seconds / 60
                val hours = minutes / 60
                view.findViewById<TextView>(R.id.timeAgoValueTextView)
                    .text = "$hours hours ago"
              }
        }
    return view
  }
}