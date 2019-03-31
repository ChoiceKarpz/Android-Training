package au.com.gridstone.trainingkotlin

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MyRecyclerViewAdapter(private val myDataset: Array<String>) :
        RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val view: FrameLayout) : RecyclerView.ViewHolder(view)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyRecyclerViewAdapter.MyViewHolder {
        // create a new view
        val row = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_row, parent, false) as FrameLayout
        // set the view's size, margins, paddings and layout parameters
        row.setPadding(0,0,0,4)

        return MyViewHolder(row)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        var textView = holder.view.findViewById<TextView>(R.id.imageListTitle)
        textView.text = myDataset[position]
        textView.background.alpha = 200
        textView.setGravity(Gravity.CENTER_VERTICAL);
        var imageView = holder.view.findViewById<ImageView>(R.id.imageListImageView)
        imageView.adjustViewBounds = true
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}