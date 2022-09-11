package com.ravenliao.navigationshareelementexample.model

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.ravenliao.navigationshareelementexample.R

const val POSITION_KEY = "position"

fun Context.getDrawableList(): List<Drawable> = listOf(
    drawable(R.drawable.ic_account_circle_black),
    drawable(R.drawable.ic_account_circle_red),
    drawable(R.drawable.ic_account_circle_white),
    drawable(R.drawable.ic_adb_black),
    drawable(R.drawable.ic_adb_white),
    drawable(R.drawable.ic_adb_red),
    drawable(R.drawable.ic_ballot_black),
    drawable(R.drawable.ic_ballot_red),
    drawable(R.drawable.ic_ballot_white),
    drawable(R.drawable.ic_contact_mail_red),
    drawable(R.drawable.ic_contact_mail_white),
    drawable(R.drawable.ic_contact_mail_black),

    )

fun Context.drawable(@DrawableRes res: Int): Drawable = AppCompatResources.getDrawable(this, res)
    ?: throw IllegalArgumentException("Drawable $res isn't exist!")

