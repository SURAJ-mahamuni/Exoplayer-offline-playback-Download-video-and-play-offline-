package com.phntechnology.exoplayerdownloader.helper

sealed class PickDocumentType{
    data class Image(val type : String) : PickDocumentType()
    data class ImageAndPdf(val type : String) : PickDocumentType()
}