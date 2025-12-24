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
        <view class="search-input brutal-btn">
          <input 
            v-model="keyword" 
            class="input" 
            placeholder="搜索感兴趣的内容..." 
            @confirm="onSearch"
          />
        </view>
      </view>
    </view>
    
    <scroll-view scroll-y class="content-scroll">
      <view v-if="!results.length" class="hot-search brutal-card">
        <text class="title">热门搜索</text>
        <view class="hot-tags">
          <view 
            v-for="tag in hotTags" 
            :key="tag" 
            class="hot-tag brutal-btn"
            @click="keyword = tag; onSearch()"
          >
            {{ tag }}
          </view>
        </view>
      </view>
      
      <view v-else class="waterfall">
        <view class="column left-column">
          <BrutalCard v-for="post in leftColumnPosts" :key="post.id" :post="post" />
        </view>
        <view class="column right-column">
          <BrutalCard v-for="post in rightColumnPosts" :key="post.id" :post="post" />
        </view>
      </view>
      
      <view class="safe-area-bottom" style="height: 100rpx;" />
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import BrutalCard from '@/components/BrutalCard/BrutalCard.vue'

const isDark = ref(uni.getStorageSync('isDark') || false)
const statusBarHeight = ref(0)
const keyword = ref('')
const hotTags = ['新野兽主义', '机能风', '成都咖啡', 'Vue3实战', '小红书排版']
const results = ref([])
const leftColumnPosts = ref([])
const rightColumnPosts = ref([])
let leftHeight = 0
let rightHeight = 0

onMounted(() => {
  const sys = uni.getSystemInfoSync()
  statusBarHeight.value = sys.statusBarHeight
})

const onSearch = async () => {
  if (!keyword.value) return
  try {
    const data = await request({ url: `/api/search/posts?keyword=${encodeURIComponent(keyword.value)}` })
    results.value = data.content
    distributePosts(data.content.map(p => ({
      ...p,
      imageUrls: typeof p.imageUrls === 'string' ? JSON.parse(p.imageUrls) : (p.imageUrls || [])
    })))
  } catch (err) {}
}

const distributePosts = (newPosts) => {
  leftColumnPosts.value = []
  rightColumnPosts.value = []
  leftHeight = 0
  rightHeight = 0
  newPosts.forEach(post => {
    if (leftHeight <= rightHeight) {
      leftColumnPosts.value.push(post)
      leftHeight += 400
    } else {
      rightColumnPosts.value.push(post)
      rightHeight += 400
    }
  })
}

const goBack = () => uni.navigateBack()
</script>

<style lang="scss" scoped>
.page-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: var(--bg-main);
}

.custom-header {
  background-color: var(--surface);
  border-bottom: 4rpx solid var(--border-color);
  
  .nav-bar {
    height: 100rpx;
    display: flex;
    align-items: center;
    padding: 0 30rpx;
    gap: 20rpx;
    
    .back-btn {
      width: 70rpx;
      height: 70rpx;
      border-radius: 16rpx;
    }
    
    .search-input {
      flex: 1;
      height: 70rpx;
      border-radius: 35rpx;
      padding: 0 30rpx;
      .input {
        width: 100%;
        font-size: 26rpx;
      }
    }
  }
}

.hot-search {
  margin: 30rpx;
  padding: 30rpx;
  .title {
    font-size: 32rpx;
    font-weight: 900;
    display: block;
    margin-bottom: 30rpx;
  }
  .hot-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 20rpx;
    .hot-tag {
      padding: 10rpx 30rpx;
      font-size: 24rpx;
      border-radius: 30rpx;
    }
  }
}

.waterfall {
  display: flex;
  padding: 20rpx;
  gap: 20rpx;
  .column { flex: 1; display: flex; flex-direction: column; gap: 20rpx; }
}
</style>

