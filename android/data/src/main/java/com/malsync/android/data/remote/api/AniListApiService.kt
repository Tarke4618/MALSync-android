package com.malsync.android.data.remote.api

import com.malsync.android.data.remote.dto.anilist.AniListUserProfileResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * AniList GraphQL API Service
 * https://anilist.gitbook.io/anilist-apiv2-docs/
 */
interface AniListApiService {
    
    @POST(".")
    @Headers("Content-Type: application/json")
    suspend fun query(
        @Body body: Map<String, Any>
    ): Response<Map<String, Any>>

    @POST(".")
    @Headers("Content-Type: application/json")
    suspend fun getAnimeList(
        @Body body: Map<String, Any>
    ): Response<AniListMediaListCollectionResponse>
    
    @POST(".")
    @Headers("Content-Type: application/json")
    suspend fun getUserProfile(
        @Body body: Map<String, String> = mapOf("query" to GET_VIEWER_QUERY)
    ): Response<AniListUserProfileResponse>
    

    companion object {
        // GraphQL queries
        // Note: $ is escaped as ${'$'} to avoid Kotlin string interpolation
        const val USER_ANIME_LIST_QUERY = """
            query (${'$'}userId: Int, ${'$'}type: MediaType, ${'$'}status: MediaListStatus) {
              MediaListCollection(userId: ${'$'}userId, type: ${'$'}type, status: ${'$'}status) {
                lists {
                  entries {
                    id
                    mediaId
                    status
                    score
                    progress
                    repeat
                    updatedAt
                    media {
                      id
                      title {
                        romaji
                        english
                        native
                      }
                      coverImage {
                        large
                        medium
                      }
                      bannerImage
                      format
                      status
                      episodes
                      duration
                      season
                      seasonYear
                      averageScore
                      meanScore
                      genres
                      studios {
                        nodes {
                          name
                        }
                      }
                      nextAiringEpisode {
                        episode
                        airingAt
                      }
                    }
                  }
                }
              }
            }
        """
        
        const val SEARCH_ANIME_QUERY = """
            query (${'$'}search: String, ${'$'}type: MediaType, ${'$'}page: Int, ${'$'}perPage: Int) {
              Page(page: ${'$'}page, perPage: ${'$'}perPage) {
                pageInfo {
                  total
                  currentPage
                  lastPage
                  hasNextPage
                }
                media(search: ${'$'}search, type: ${'$'}type) {
                  id
                  title {
                    romaji
                    english
                    native
                  }
                  coverImage {
                    large
                    medium
                  }
                  bannerImage
                  format
                  status
                  episodes
                  season
                  seasonYear
                  averageScore
                  genres
                }
              }
            }
        """
        
        const val GET_ANIME_QUERY = """
            query (${'$'}id: Int) {
              Media(id: ${'$'}id, type: ANIME) {
                id
                title {
                  romaji
                  english
                  native
                }
                synonyms
                coverImage {
                  large
                  medium
                }
                bannerImage
                format
                status
                description
                episodes
                duration
                season
                seasonYear
                averageScore
                meanScore
                genres
                studios {
                  nodes {
                    name
                  }
                }
                nextAiringEpisode {
                  episode
                  airingAt
                }
                streamingEpisodes {
                  title
                  thumbnail
                  url
                  site
                }
              }
            }
        """
        
        const val UPDATE_ANIME_MUTATION = """
            mutation (${'$'}mediaId: Int, ${'$'}status: MediaListStatus, ${'$'}score: Float, ${'$'}progress: Int) {
              SaveMediaListEntry(mediaId: ${'$'}mediaId, status: ${'$'}status, score: ${'$'}score, progress: ${'$'}progress) {
                id
                mediaId
                status
                score
                progress
                updatedAt
              }
            }
        """
        
        const val DELETE_ANIME_MUTATION = """
            mutation (${'$'}id: Int) {
              DeleteMediaListEntry(id: ${'$'}id) {
                deleted
              }
            }
        """
        
        const val GET_VIEWER_QUERY = """
            query {
              Viewer {
                id
                name
                avatar {
                  large
                  medium
                }
                statistics {
                  anime {
                    count
                    meanScore
                    minutesWatched
                  }
                  manga {
                    count
                    meanScore
                    chaptersRead
                  }
                }
              }
            }
        """
    }
}
