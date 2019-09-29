package com.example.onesns.chatAdapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.onesns.R
import com.example.onesns.chatFragment.ChatsFragment
import com.example.onesns.chatFragment.RequestsFragment

class TabsPagerAdapter(fm: FragmentManager, internal var context: Context) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return ChatsFragment()
            }
            1 -> {
                return RequestsFragment()
            }
            else -> return Fragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return context.getString(R.string.chat_room) // ChatsFragment
            1 -> return context.getString(R.string.request_room) // RequestsFragment
            else -> return null
        }
    }
}
