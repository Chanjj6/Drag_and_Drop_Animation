package com.example.drag_and_drop_animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.example.drag_and_drop_animation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val x = binding.holder01.setOnDragListener(arrowDragListener)
        binding.holder02.setOnDragListener(arrowDragListener)
        //adding the other holders in
        binding.holder03.setOnDragListener(arrowDragListener)
        binding.holder04.setOnDragListener(arrowDragListener)



        //setting listeners for the arrows
        binding.upMoveBtn.setOnLongClickListener(onLongClickListener)
        binding.downMoveBtn.setOnLongClickListener(onLongClickListener)
        binding.forwardMoveBtn.setOnLongClickListener(onLongClickListener)
        binding.backMoveBtn.setOnLongClickListener(onLongClickListener)
        binding.figure.setBackgroundResource(R.drawable.animation)
        val Animation = binding.figure.background as AnimationDrawable
        if (Animation.isRunning) {
            Animation.stop()
        } else {
            Animation.start()
        }
        binding.play.setOnClickListener {
            move()

        }



    }

    private fun move(){
        val animset = AnimatorSet()
        val movePerson = ObjectAnimator.ofFloat(binding.figure,"translationX", 300f)
        val rotatePerson = ObjectAnimator.ofFloat(binding.figure,"rotation", 90f)
        val movePerson2 = ObjectAnimator.ofFloat(binding.figure,"translationY", 500f)
        val rotatePerson2 = ObjectAnimator.ofFloat(binding.figure,"rotation", 270f)
        val movePerson3 = ObjectAnimator.ofFloat(binding.figure,"translationY", -900f)
        animset.play(movePerson)
        animset.play(rotatePerson).after(movePerson)
        animset.play(movePerson2).after(rotatePerson)
        animset.play(rotatePerson2).after(movePerson2)
        animset.play(movePerson3).after(rotatePerson2)
        animset.start()
    }




    private val onLongClickListener = View.OnLongClickListener { view: View ->
        (view as? Button)?.let {

            val item = ClipData.Item(it.tag as? CharSequence)

            val dragData = ClipData( it.tag as? CharSequence,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), item)
            val myShadow = ArrowDragShadowBuilder(it)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.startDragAndDrop(dragData, myShadow, null, 0)
            } else {
                it.startDrag(dragData, myShadow, null, 0)
            }

            true
        }
        false
    }




    private val arrowDragListener = View.OnDragListener { view, dragEvent ->
        (view as? ImageView)?.let {
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    view.setImageResource(R.drawable.highlight)
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_EXITED-> {
                    view.setImageResource(R.drawable.rec)
                    return@OnDragListener true
                }
                // No need to handle this for our use case.
                DragEvent.ACTION_DRAG_LOCATION -> {
                    return@OnDragListener true
                }

                DragEvent.ACTION_DROP -> {
                    view.setImageResource(R.drawable.rec)
                    // Read color data from the clip data and apply it to the card view background.
                    val item: ClipData.Item = dragEvent.clipData.getItemAt(0)
                    val lbl = item.text.toString()
                    Log.d("BCCCCCCCCCCC", "NOTHING > >  " + lbl)
                    when(lbl.toString()){
                        "UP"->view.setImageResource( R.drawable.ic_arrow_upward)
                        "DOWN"->view.setImageResource( R.drawable.ic_arrow_downward)
                        "FORWARD"->view.setImageResource( R.drawable.ic_arrow_forward)
                        "BACK"->view.setImageResource( R.drawable.ic_arrow_back)
                    }
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    return@OnDragListener true
                }
                else -> return@OnDragListener false
            }
        }
        false
    }


    private class ArrowDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {
        private val shadow = view.background
        override fun onProvideShadowMetrics(size: Point, touch: Point) {
            val width: Int = view.width
            val height: Int = view.height
            shadow?.setBounds(0, 0, width, height)
            size.set(width, height)
            touch.set(width / 2, height / 2)
        }
        override fun onDrawShadow(canvas: Canvas) {
            shadow?.draw(canvas)
        }
    }
}