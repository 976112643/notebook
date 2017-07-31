package com.wq.common.util

/**
 * 日誌等級枚舉  _ALL/_NONE 全部打印/不打印
 * 避免枚舉類型類型與其他方法及變量等重名，加入_作為區分
 * Created by weiquan on 2017/7/14.
 */
enum class LEVEL {
    _V,_A,_I,_D,_W,_E,_ALL,_NONE
}
