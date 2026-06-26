

package com.urlshortener.dto.response;

import java.util.List;

public class AnalyticsResponse {

    private String shortCode;
    private long totalClicks;
    private List<ClickDetail> recentClicks;

    public AnalyticsResponse() {
    }

    public AnalyticsResponse(String shortCode, long totalClicks, List<ClickDetail> recentClicks) {
        this.shortCode = shortCode;
        this.totalClicks = totalClicks;
        this.recentClicks = recentClicks;
    }

    public String getShortCode() { return shortCode; }
    public long getTotalClicks() { return totalClicks; }
    public List<ClickDetail> getRecentClicks() { return recentClicks; }

    public void setShortCode(String shortCode) { this.shortCode = shortCode; }
    public void setTotalClicks(long totalClicks) { this.totalClicks = totalClicks; }
    public void setRecentClicks(List<ClickDetail> recentClicks) { this.recentClicks = recentClicks; }

    public static class ClickDetail {

        private String ipAddress;
        private String clickedAt;

        public ClickDetail() {
        }

        public ClickDetail(String ipAddress, String clickedAt) {
            this.ipAddress = ipAddress;
            this.clickedAt = clickedAt;
        }

        public String getIpAddress() { return ipAddress; }
        public String getClickedAt() { return clickedAt; }

        public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
        public void setClickedAt(String clickedAt) { this.clickedAt = clickedAt; }
    }
}