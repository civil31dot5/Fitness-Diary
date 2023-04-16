package com.civil31dot5.fitnessdiary.ui.utility

interface GenericRecordAdapterListener<T> {

    fun onEditClick(data: T)
    fun onDeleteClick(data: T)

}