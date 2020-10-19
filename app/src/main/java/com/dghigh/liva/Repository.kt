package com.dghigh.liva

object Repository {

    var position:Int = 0
    var pic = R.drawable.ba1

    val listPic6 = listOf(
        R.drawable.ba1,
        R.drawable.ba2,
        R.drawable.ba3,
        R.drawable.ba4,
        R.drawable.ba5,
        R.drawable.ba6,
        R.drawable.ba7
    )
    val listPic4 = listOf(
        R.drawable.be1,
        R.drawable.be2,
        R.drawable.be3,
        R.drawable.be4,
        R.drawable.be5,
        R.drawable.be6,
        R.drawable.be7
    )
    val listPic1 = listOf(
        R.drawable.fo1,
        R.drawable.fo2,
        R.drawable.fo3,
        R.drawable.fo4,
        R.drawable.fo5,
        R.drawable.fo6,
        R.drawable.fo7
    )
    val listPic9 = listOf(
        R.drawable.go1,
        R.drawable.go2,
        R.drawable.go3,
        R.drawable.go4,
        R.drawable.go5,
        R.drawable.go6,
        R.drawable.go7
    )
    val listPic2 = listOf(
        R.drawable.ho1,
        R.drawable.ho2,
        R.drawable.ho3,
        R.drawable.ho4,
        R.drawable.ho5,
        R.drawable.ho6,
        R.drawable.ho7
    )
    val listPic5 = listOf(
        R.drawable.kr1,
        R.drawable.kr2,
        R.drawable.kr3,
        R.drawable.kr4,
        R.drawable.kr5,
        R.drawable.kr6,
        R.drawable.kr7
    )
    val listPic8 = listOf(
        R.drawable.mm1,
        R.drawable.mm2,
        R.drawable.mm3,
        R.drawable.mm4,
        R.drawable.mm5,
        R.drawable.mm6,
        R.drawable.mm7
    )
    val listPic3 = listOf(
        R.drawable.te1,
        R.drawable.te2,
        R.drawable.te3,
        R.drawable.te4,
        R.drawable.te5,
        R.drawable.te6,
        R.drawable.te7
    )
    val listPic7 = listOf(
        R.drawable.vo1,
        R.drawable.vo2,
        R.drawable.vo3,
        R.drawable.vo4,
        R.drawable.vo5,
        R.drawable.vo6,
        R.drawable.vo7
    )

    fun getListPic(position: Int) : List<Int> {
        var list : List<Int> = listPic1
        when(position) {
            0 -> list = listPic1
            1 -> list = listPic2
            2 -> list = listPic3
            3 -> list = listPic4
            4 -> list = listPic5
            5 -> list = listPic6
            6 -> list = listPic7
            7 -> list = listPic8
            8 -> list = listPic9
        }
        return list
    }
}