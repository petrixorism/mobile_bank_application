package uz.gita.myapplication.util

import android.text.Editable
import com.google.android.material.internal.TextWatcherAdapter

class ExpirationFormatWatcher : TextWatcherAdapter() {

    override fun afterTextChanged(s: Editable) {
        super.afterTextChanged(s)

        if (s.isEmpty()) return

        s.forEachIndexed { index, c ->
            val spaceIndex = index == 2
            when {
                !spaceIndex -> s.delete(index, index + 1)
                spaceIndex -> s.insert(index, "/")
            }
        }
        if (s.isNotEmpty()) {
            if (s.last().isWhitespace())
                s.delete(s.length - 1, s.length)
        }

    }


}