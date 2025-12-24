<template>
  <view :class="{ 'dark': isDark }" class="page-container">
    <view class="custom-header">
      <view class="status-bar" :style="{ height: statusBarHeight + 'px' }" />
      <view class="nav-bar">
        <view class="back-btn brutal-btn" @click="goBack"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><polyline points="15 18 9 12 15 6"></polyline></svg></view>
        <text class="title">赞与收藏</text>
      </view>
    </view>
    <scroll-view scroll-y class="content-scroll">
      <view v-for="i in 5" :key="i" class="notify-item brutal-card">
        <image src="https://picsum.photos/80/80?random=10" class="avatar" />
        <view class="info">
          <text class="user">用户 {{ i }}</text>
          <text class="action">赞了你的笔记</text>
          <text class="time">12-24</text>
        </view>
        <image src="https://picsum.photos/100/100?random=5" class="post-preview brutal-card" />
      </view>
    </scroll-view>
  </view>
</template>
<script setup>
import { ref } from 'vue'; const isDark = ref(uni.getStorageSync('isDark') || false); const statusBarHeight = ref(uni.getSystemInfoSync().statusBarHeight)
const goBack = () => uni.navigateBack()
</script>
<style lang="scss" scoped>
.page-container { height: 100vh; display: flex; flex-direction: column; background: var(--bg-main); }
.custom-header { background: var(--surface); border-bottom: 4rpx solid #000; .nav-bar { height: 100rpx; display: flex; align-items: center; padding: 0 30rpx; gap: 30rpx; .back-btn { width: 70rpx; height: 70rpx; border-radius: 16rpx; } .title { font-size: 32rpx; font-weight: 800; } } }
.notify-item { margin: 20rpx; padding: 24rpx; display: flex; align-items: center; gap: 20rpx; .avatar { width: 80rpx; height: 80rpx; border-radius: 50%; border: 2rpx solid #000; } .info { flex: 1; .user { font-size: 28rpx; font-weight: 800; display: block; } .action { font-size: 24rpx; opacity: 0.7; } .time { font-size: 20rpx; opacity: 0.4; display: block; } } .post-preview { width: 100rpx; height: 100rpx; border-radius: 12rpx; } }
</style>

