<template>
  <view :class="{ 'dark': isDark }" class="page-container">
    <view class="custom-header">
      <view class="status-bar" :style="{ height: statusBarHeight + 'px' }" />
      <view class="nav-bar">
        <view class="back-btn brutal-btn" @click="goBack"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><polyline points="15 18 9 12 15 6"></polyline></svg></view>
        <text class="title">新增关注</text>
      </view>
    </view>
    <scroll-view scroll-y class="content-scroll">
      <view v-for="i in 3" :key="i" class="notify-item brutal-card">
        <image src="https://picsum.photos/80/80?random=30" class="avatar brutal-card" />
        <view class="info">
          <text class="user">新粉丝 {{ i }}</text>
          <text class="action">开始关注你了</text>
          <text class="time">12-24</text>
        </view>
        <view class="follow-btn brutal-btn primary">回关</view>
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
.content-scroll { flex: 1; padding: 20rpx; box-sizing: border-box; }
.notify-item { margin-bottom: 24rpx; padding: 24rpx; display: flex; align-items: center; gap: 24rpx; background: var(--surface); box-shadow: 6rpx 6rpx 0 #000;
  .avatar { width: 90rpx; height: 90rpx; border-radius: 50%; flex-shrink: 0; } 
  .info { flex: 1; min-width: 0;
    .user { font-size: 30rpx; font-weight: 800; display: block; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; } 
    .action { font-size: 24rpx; opacity: 0.7; } 
    .time { font-size: 20rpx; opacity: 0.4; display: block; } 
  } 
  .follow-btn { width: 130rpx; height: 64rpx; font-size: 24rpx; flex-shrink: 0; box-shadow: 4rpx 4rpx 0 #000; } 
}
</style>
