<template>
  <view class="tab-bar-container safe-area-bottom">
    <view class="tab-bar">
      <view 
        v-for="(item, index) in tabs" 
        :key="index"
        class="tab-item"
        :class="{ 'active': activeTab === item.key }"
        @click="handleNav(item.key)"
      >
        <template v-if="item.key === 'publish'">
          <view class="publish-btn">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="4">
              <line x1="12" y1="5" x2="12" y2="19"></line>
              <line x1="5" y1="12" x2="19" y2="12"></line>
            </svg>
          </view>
        </template>
        <template v-else>
          <view class="label-box">
            <text class="tab-label">{{ item.label }}</text>
            <view v-if="item.key === 'message' && bus.unreadCount > 0" class="red-dot">{{ bus.unreadCount }}</view>
          </view>
          <view v-if="activeTab === item.key" class="active-indicator" />
        </template>
      </view>
    </view>
  </view>
</template>

<script setup>
import { bus } from '@/utils/bus'
import request from '@/utils/request'
import { onMounted } from 'vue'

const props = defineProps({
  activeTab: { type: String, default: 'home' }
})

const tabs = [
  { label: '首页', key: 'home' },
  { label: '分区', key: 'sections' },
  { label: '', key: 'publish' },
  { label: '消息', key: 'message' },
  { label: '我', key: 'me' }
]

onMounted(async () => {
  const token = uni.getStorageSync('token')
  if (token) {
    try {
      const data = await request({ url: '/api/notifications/unread-count' })
      bus.updateUnreadCount(data.chat + data.interaction + data.system)
    } catch (e) {}
  }
})

const handleNav = (key) => {
  const token = uni.getStorageSync('token')
  if (['publish', 'message', 'me'].includes(key) && !token) {
    uni.showToast({ title: '请先登录', icon: 'none' })
    setTimeout(() => bus.openLogin(), 800)
    return
  }
  if (key === props.activeTab && key !== 'publish') return
  const path = key === 'home' ? 'index' : key
  uni.reLaunch({ url: `/pages/${path}/${path}` })
}
</script>

<style lang="scss" scoped>
.tab-bar-container { position: fixed; bottom: 40rpx; left: 30rpx; right: 30rpx; z-index: 1000; }
.tab-bar { display: flex; height: 110rpx; background: var(--surface); border: 4rpx solid #000; box-shadow: 8rpx 8rpx 0 #000; border-radius: 60rpx; align-items: center; justify-content: space-around; }
.tab-item { flex: 1; display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100%; position: relative; }
.label-box { position: relative; .tab-label { font-size: 28rpx; font-weight: 800; color: var(--text-main); } }
.red-dot { position: absolute; top: -10rpx; right: -25rpx; background: var(--accent-red); color: #fff; font-size: 18rpx; padding: 0 8rpx; border-radius: 20rpx; border: 2rpx solid #000; font-weight: 800; }
.active .tab-label { color: var(--primary); transform: scale(1.1); }
.active-indicator { position: absolute; bottom: 12rpx; width: 30rpx; height: 6rpx; background: #000; transform: skewX(-20deg); }
.publish-btn { width: 84rpx; height: 84rpx; background: var(--primary); border: 4rpx solid #000; box-shadow: 4rpx 4rpx 0 #000; border-radius: 24rpx; display: flex; align-items: center; justify-content: center; color: #fff; &:active { transform: translate(2rpx, 2rpx); box-shadow: 2rpx 2rpx 0 #000; } }
</style>
