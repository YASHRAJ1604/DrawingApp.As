package com.example.drwaingappas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class DrawingView (context: Context, arrts: AttributeSet): View(context,arrts){
    private var mDrawPath:CustomPath?=null
    private var mCanvasBitmap: Bitmap?=null
    private var mDrawpaint: Paint?=null
    private var mCanvasPaint: Paint?=null
    private var mBrushsize:Float=0. toFloat()
    private var color= Color.BLACK
    private var canvas: Canvas?=null
    private val mPaths=ArrayList<CustomPath>()
    private val mUndoPaths=ArrayList<CustomPath>()

    init {
        setDrawing()
    }
    fun onclickUndo(){
        if (mPaths.size > 0 ){
            mUndoPaths.add(mPaths.removeAt(mPaths.size - 1))
            invalidate()
        }
    }


    private fun setDrawing(){
        mDrawpaint= Paint()
        mDrawPath= CustomPath(color,mBrushsize)
        mDrawpaint!!.color=color
        mDrawpaint!!.style= Paint.Style.STROKE
        mDrawpaint!!.strokeJoin= Paint.Join.ROUND
        mDrawpaint!!.strokeCap= Paint.Cap.ROUND
        //  mBrushsize=20.  toFloat()
        mCanvasPaint= Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap= Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888)
        canvas= Canvas(mCanvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!,0f,0f,mCanvasPaint)
        for (paths in mPaths){
            mDrawpaint!!.strokeWidth=paths.BrushThickness
            mDrawpaint!!.color=paths.color
            canvas.drawPath(paths,mDrawpaint!!)

        }
        if (!mDrawPath!!.isEmpty){
            mDrawpaint!!.strokeWidth=mDrawPath!!.BrushThickness
            mDrawpaint!!.color=mDrawPath!!.color
            canvas.drawPath(mDrawPath!!,mDrawpaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX=event?.x
        val touchY=event?.y

        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                mDrawPath!!.color=color
                mDrawPath!!.BrushThickness=mBrushsize
                mDrawPath!!.reset()
                if (touchX!=null){
                    if (touchY!=null) mDrawPath!!.moveTo(touchX!!,touchY!!)
                }

            }
            MotionEvent.ACTION_MOVE->{
                if (touchX!=null){
                    if (touchY!=null){
                        mDrawPath!!.lineTo(touchX!!,touchY!!)
                    }
                }
            }
            MotionEvent.ACTION_UP->{
                mPaths.add(mDrawPath!!)
                mDrawPath= CustomPath(color,mBrushsize)
            }
            else->return false


        }
        invalidate()
        return true


        return super.onTouchEvent(event)
    }
    fun brushsize(newsize:Float){
        mBrushsize= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,newsize,resources.displayMetrics)
        mDrawpaint!!.strokeWidth=mBrushsize
    }

    fun setColor(newColor: String) {
        color= Color.parseColor(newColor)
        mDrawpaint!!.color=color

    }


}



class CustomPath(var color:Int,var BrushThickness:Float) : Path() {

}
