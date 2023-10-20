package com.n1.moguchi.ui

interface ChildCardListener {
    fun retrieveChildrenNamesFromCard(childrenByCardPosition: MutableMap<Int, String>)
}