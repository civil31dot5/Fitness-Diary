package com.civil31dot5.fitnessdiary.ui.utility

import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler

class FixedLabelXAxisRender(
    viewPortHandler: ViewPortHandler,
    xAxis: XAxis,
    trans: Transformer,
    private val forceXAxisValue: FloatArray? = null
) : XAxisRenderer(viewPortHandler, xAxis, trans) {

    override fun computeAxisValues(min: Float, max: Float) {
        super.computeAxisValues(min, max)
        forceXAxisValue?.let {
            mAxis.mEntryCount = it.size
            mAxis.mEntries = it.clone()
        }
    }
}