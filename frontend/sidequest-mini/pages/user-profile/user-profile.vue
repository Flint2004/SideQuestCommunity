<template>
  <view :class="{ 'dark': isDark }" class="page-container">
    <view class="custom-header">
      <view class="status-bar" :style="{ height: statusBarHeight + 'px' }" />
      <view class="nav-bar">
        <view class="back-btn brutal-btn" @click="goBack">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3">
            <polyline points="15 18 9 12 15 6"></polyline>
          </svg>
        </view>
        <text class="title">{{ user.nickname }}</text>
      </view>
    </view>
    
    <scroll-view scroll-y class="content-scroll">
      <view class="profile-card brutal-card">
        <image :src="user.avatar" class="avatar brutal-card" />
        <view class="info">
          <text class="nickname">{{ user.nickname }}</text>
          <view class="stats">
            <view class="stat-item"><text class="v">{{ user.followingCount }}</text><text class="l">关注</text></view>
            <view class="stat-item"><text class="v">{{ user.followerCount }}</text><text class="l">粉丝</text></view>
            <view class="stat-item"><text class="v">{{ user.totalLikedCount }}</text><text class="l">获赞</text></view>
          </view>
        </view>
        <view class="actions">
          <view class="action-btn brutal-btn primary" @click="toggleFollow">
            {{ user.isFollowing ? '已关注' : '关注' }}
          </view>
          <view class="action-btn brutal-btn" @click="goToChat">私信</view>
        </view>
      </view>
      
      <view class="waterfall">
        <view class="column left-column">
          <BrutalCard v-for="post in leftColumnPosts" :key="post.id" :post="post" @click="goToDetail(post.id)" />
        </view>
        <view class="column right-column">
          <BrutalCard v-for="post in rightColumnPosts" :key="post.id" :post="post" @click="goToDetail(post.id)" />
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import BrutalCard from '@/components/BrutalCard/BrutalCard.vue'

const isDark = ref(uni.getStorageSync('isDark') || false)
const statusBarHeight = ref(0)
const user = ref({})
const leftColumnPosts = ref([])
const rightColumnPosts = ref([])
let leftHeight = 0
let rightHeight = 0

onMounted(async () => {
  const sys = uni.getSystemInfoSync()
  statusBarHeight.value = sys.statusBarHeight
  
  const pages = getCurrentPages()
  const userId = pages[pages.length - 1].options.userId || 102
  
  try {
    const userInfo = await request({ url: `/api/identity/users/${userId}/public` })
    user.value = userInfo
    
    const data = await request({ url: `/api/search/user/posts?userId=${userId}` })
    distributePosts(data.content.map(p => ({
      ...p,
      imageUrls: typeof p.imageUrls === 'string' ? JSON.parse(p.imageUrls) : (p.imageUrls || [])
    })))
  } catch (err) {}
})

const distributePosts = (newPosts) => {
  newPosts.forEach(post => {
    const match = post.imageUrls[0]?.match(/_w(\d+)_h(\d+)/)
    const h = match ? 340 * Math.max(0.75, Math.min(1.33, match[2]/match[1])) : 400
    if (leftHeight <= rightHeight) {
      leftColumnPosts.value.push(post)
      leftHeight += h
    } else {
      rightColumnPosts.value.push(post)
      rightHeight += h
    }
  })
}

const toggleFollow = () => user.value.isFollowing = !user.value.isFollowing
const goToChat = () => uni.navigateTo({ url: `/pages/chat/chat?roomId=1` })
const goToDetail = (id) => uni.navigateTo({ url: `/pages/post-detail/post-detail?id=${id}` })
const goBack = () => uni.navigateBack()
</script>

<style lang="scss" scoped>
.page-container { height: 100vh; display: flex; flex-direction: column; background-color: var(--bg-main); }
.custom-header { background-color: var(--surface); border-bottom: 4rpx solid var(--border-color); .nav-bar { height: 100rpx; display: flex; align-items: center; padding: 0 30rpx; gap: 30rpx; .back-btn { width: 70rpx; height: 70rpx; border-radius: 16rpx; } .title { font-size: 32rpx; font-weight: 800; } } }
.profile-card { margin: 30rpx; padding: 40rpx; background-color: var(--surface); display: flex; flex-direction: column; align-items: center; gap: 30rpx; .avatar { width: 160rpx; height: 160rpx; border-radius: 50%; } .info { align-items: center; display: flex; flex-direction: column; .nickname { font-size: 40rpx; font-weight: 900; margin-bottom: 20rpx; } .stats { display: flex; gap: 40rpx; .stat-item { display: flex; flex-direction: column; align-items: center; .v { font-size: 32rpx; font-weight: 900; } .l { font-size: 20rpx; opacity: 0.6; } } } } .actions { display: flex; gap: 20rpx; width: 100%; .action-btn { flex: 1; height: 80rpx; font-size: 28rpx; } } }
.waterfall { display: flex; padding: 20rpx; gap: 20rpx; .column { flex: 1; display: flex; flex-direction: column; gap: 20rpx; } }
</style>

