package com.luciasoft.browser

enum class FontMode(private val value: Int)
{
    System(1), AppDefault(2), CustomOrSystem(3), CustomOrAppDefault(4);

    override fun toString(): String
    {
        return "$value:$name"
    }

    companion object
    {
        @JvmStatic
        fun valueOf(fontMode: Int): FontMode
        {
            return when (fontMode)
            {
                1 -> System
                2 -> AppDefault
                3 -> CustomOrSystem
                4 -> CustomOrAppDefault
                else -> throw Exception()
            }
        }
    }
}

enum class ScreenMode(private val value: Int)
{
    NotSpecified(1), SystemDefault(2), AllowPortraitUprightAndLandscape(3),
    AllowPortraitUprightOnly(4), AllowLandscapeOnly(5), AllowAll(6);

    override fun toString(): String
    {
        return "$value:$name"
    }

    companion object
    {
        @JvmStatic
        fun valueOf(screenMode: Int): ScreenMode
        {
            return when (screenMode)
            {
                1 -> NotSpecified
                2 -> SystemDefault
                3 -> AllowPortraitUprightAndLandscape
                4 -> AllowPortraitUprightOnly
                5 -> AllowLandscapeOnly
                6 -> AllowAll
                else -> throw Exception()
            }
        }
    }
}

enum class BrowseMode(private val value: Int)
{
    LoadFilesAndOrFolders(1), SaveFilesAndOrFolders(2),
    LoadFolders(3), SaveFolders(4);

    override fun toString(): String
    {
        return "$value:$name"
    }

    companion object
    {
        @JvmStatic
        fun valueOf(browseMode: Int): BrowseMode
        {
            return when (browseMode)
            {
                1 -> LoadFilesAndOrFolders
                2 -> SaveFilesAndOrFolders
                3 -> LoadFolders
                4 -> SaveFolders
                else -> throw Exception()
            }
        }
    }
}

enum class BrowserViewType(private val value: Int)
{
    List(1), Tiles(2), Gallery(3);

    override fun toString(): String
    {
        return "$value:$name"
    }

    companion object
    {
        @JvmStatic
        fun valueOf(browserViewType: Int): BrowserViewType
        {
            return when (browserViewType)
            {
                1 -> List
                2 -> Tiles
                3 -> Gallery
                else -> throw Exception()
            }
        }
    }
}
enum class SortOrder(private val value: Int)
{
    PathAscending(1), PathDescending(2), DateAscending(3),
    DateDescending(4), SizeAscending(5), SizeDescending(6);

    override fun toString(): String
    {
        return "$value:$name"
    }

    companion object
    {
        @JvmStatic
        fun valueOf(sortOrder: Int): SortOrder
        {
            return when (sortOrder)
            {
                1 -> PathAscending
                2 -> PathDescending
                3 -> DateAscending
                4 -> DateDescending
                5 -> SizeAscending
                6 -> SizeDescending
                else -> throw Exception()
            }
        }
    }
}

enum class SaveFileBehavior(private val value: Int)
{
    SaveFile(1), SendNameToSaveBoxOrSaveFile(2), SendNameToSaveBoxAndSaveFile(3);

    override fun toString(): String
    {
        return "$value:$name"
    }

    companion object
    {
        @JvmStatic
        fun valueOf(saveFileBehavior: Int): SaveFileBehavior
        {
            return when (saveFileBehavior)
            {
                1 -> SaveFile
                2 -> SendNameToSaveBoxOrSaveFile
                3 -> SendNameToSaveBoxAndSaveFile
                else -> throw Exception()
            }
        }
    }
}
