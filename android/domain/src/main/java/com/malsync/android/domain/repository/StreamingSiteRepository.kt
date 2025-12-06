package com.malsync.android.domain.repository

import com.malsync.android.domain.model.DetectedContent
import com.malsync.android.domain.model.StreamingSite

/**
 * Repository for streaming site content detection
 * Ports functionality from src/pages/
 */
interface StreamingSiteRepository {
    
    /**
     * Get all supported streaming sites
     */
    suspend fun getSupportedSites(): List<StreamingSite>
    
    /**
     * Detect content from URL
     */
    suspend fun detectContent(url: String): DetectedContent?
    
    /**
     * Get injection script for a URL
     */
    suspend fun getInjectionScript(url: String): String?
    
    /**
     * Check if URL is supported
     */
    suspend fun isSiteSupported(url: String): Boolean
    
    /**
     * Get site by domain
     */
    suspend fun getSiteByDomain(domain: String): StreamingSite?
}
