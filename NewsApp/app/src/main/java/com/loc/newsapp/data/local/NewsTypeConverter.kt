package com.loc.newsapp.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.loc.newsapp.domain.model.Source

@ProvidedTypeConverter
class NewsTypeConverter {

    @TypeConverter
    fun sourceToString(source: Source): String {
        return "${source.id},${source.name}"
    }

    @TypeConverter
    fun stringToSource(data: String): Source {

        return data.split(",").let { sourceArray ->
            Source(
                id = sourceArray[0],
                name = sourceArray[1]
            )
        }
    }

}
