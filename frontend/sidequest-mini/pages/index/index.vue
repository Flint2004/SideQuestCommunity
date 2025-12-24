<template>
  <view :class="{ 'dark': isDark }" class="page-container">
    <BrutalHeader :isDark="isDark" :activeTab="activeTopTab" @tab-change="onTopTabChange" @toggle-dark="toggleDark" @search="goToSearch" />
    <scroll-view scroll-y class="content-scroll" @scrolltolower="loadMore" refresher-enabled :refresher-triggered="isRefreshing" @refresherrefresh="onRefresh">
      <view class="waterfall">
        <view class="column left-column">
          <BrutalCard v-for="post in leftColumnPosts" :key="post.id" :post="post" @click="goToDetail(post.id)" @like="handleLike" @user-click="goToUserProfile" />
        </view>
        <view class="column right-column">
          <BrutalCard v-for="post in rightColumnPosts" :key="post.id" :post="post" @click="goToDetail(post.id)" @like="handleLike" @user-click="goToUserProfile" />
        </view>
      </view>
      <view class="safe-area-bottom" style="height: 160rpx;" />
    </scroll-view>
    <BrutalTabBar activeTab="home" />
    <LoginModal />
  </view>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import request from '@/utils/request'
import { bus } from '@/utils/bus'

const isDark = ref(uni.getStorageSync('isDark') || false)
const activeTopTab = ref(1); const isRefreshing = ref(false); const loading = ref(false); const noMore = ref(false)
const leftColumnPosts = ref([]); const rightColumnPosts = ref([])
let leftHeight = 0; let rightHeight = 0

onMounted(() => {
  fetchPosts()
  uni.$on('updatePostStatus', (data) => {
    const all = [...leftColumnPosts.value, ...rightColumnPosts.value]
    const p = all.find(x => x.id === data.id)
    if (p) {
      if (data.hasLiked !== undefined) { p.hasLiked = data.hasLiked; p.likeCount = data.likeCount }
      if (data.hasFavorited !== undefined) { p.hasFavorited = data.hasFavorited; p.favoriteCount = data.favoriteCount }
    }
  })
})

onUnmounted(() => uni.$off('updatePostStatus'))

const fetchPosts = async () => {
  if (loading.value || noMore.value) return
  loading.value = true
  try {
    const data = await request({ url: `/api/core/posts?size=10` })
    if (data.records.length === 0) noMore.value = true
    else distributePosts(data.records.map(p => ({ ...p, imageUrls: typeof p.imageUrls === 'string' ? JSON.parse(p.imageUrls) : p.imageUrls })))
  } catch (e) {} finally { loading.value = false; isRefreshing.value = false }
}

const distributePosts = (newPosts) => {
  newPosts.forEach(post => {
    const match = post.imageUrls[0]?.match(/_w(\d+)_h(\d+)/); const h = match ? 340 * Math.max(0.75, Math.min(1.33, match[2]/match[1])) : 400
    if (leftHeight <= rightHeight) { leftColumnPosts.value.push(post); leftHeight += h }
    else { rightColumnPosts.value.push(post); rightHeight += h }
  })
}

const handleLike = async (id) => {
  const token = uni.getStorageSync('token'); if (!token) { bus.openLogin(); return }
  const p = [...leftColumnPosts.value, ...rightColumnPosts.value].find(x => x.id === id)
  if (p) { p.hasLiked = !p.hasLiked; p.hasLiked ? p.likeCount++ : p.likeCount-- }
  await request({ url: `/api/core/interactions/like?postId=${id}`, method: 'POST' })
}

const onRefresh = () => { leftColumnPosts.value = []; rightColumnPosts.value = []; leftHeight = 0; rightHeight = 0; fetchPosts() }
const goToDetail = (id) => uni.navigateTo({ url: `/pages/post-detail/post-detail?id=${id}` })
const goToSearch = () => uni.navigateTo({ url: '/pages/search/search' })
const goToUserProfile = (userId) => uni.navigateTo({ url: `/pages/user-profile/user-profile?userId=${userId}` })
const toggleDark = () => { isDark.value = !isDark.value; uni.setStorageSync('isDark', isDark.value) }
</script>

<style lang="scss">
.page-container { height: 100vh; display: flex; flex-direction: column; background: var(--bg-main); }
.content-scroll { flex: 1; }
.waterfall { display: flex; padding: 20rpx; gap: 20rpx; .column { flex: 1; display: flex; flex-direction: column; gap: 20rpx; } }
</style>
