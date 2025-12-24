<template>
  <view :class="{ 'dark': isDark }" class="page-container">
    <BrutalHeader 
      :isDark="isDark" 
      :activeTab="1" 
      :tabs="['游戏分区']"
      @toggle-dark="toggleDark"
      @search="goToSearch"
    />
    
    <scroll-view scroll-y class="content-scroll">
      <view v-if="!selectedSection" class="section-grid">
        <view 
          v-for="section in sections" 
          :key="section.id"
          class="section-card brutal-card"
          @click="selectSection(section)"
        >
          <text class="section-name">{{ section.displayNameZh }}</text>
          <text class="section-en">{{ section.displayNameEn }}</text>
        </view>
      </view>
      
      <view v-else class="filtered-view">
        <view class="filter-header brutal-card">
          <text class="filter-title">{{ selectedSection.displayNameZh }}</text>
          <view class="close-btn" @click="selectedSection = null">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3">
              <line x1="18" y1="6" x2="6" y2="18"></line>
              <line x1="6" y1="6" x2="18" y2="18"></line>
            </svg>
          </view>
        </view>
        
        <view class="waterfall">
          <view class="column left-column">
            <BrutalCard 
              v-for="post in leftColumnPosts" 
              :key="post.id" 
              :post="post" 
              @click="goToDetail(post.id)"
            />
          </view>
          <view class="column right-column">
            <BrutalCard 
              v-for="post in rightColumnPosts" 
              :key="post.id" 
              :post="post" 
              @click="goToDetail(post.id)"
            />
          </view>
        </view>
      </view>
      
      <view class="safe-area-bottom" style="height: 160rpx;" />
    </scroll-view>
    
    <BrutalTabBar activeTab="sections" />
    <LoginModal />
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import BrutalCard from '@/components/BrutalCard/BrutalCard.vue'
import BrutalTabBar from '@/components/BrutalTabBar/BrutalTabBar.vue'
import BrutalHeader from '@/components/BrutalHeader/BrutalHeader.vue'
import request from '@/utils/request'

const isDark = ref(uni.getStorageSync('isDark') || false)
const sections = ref([])
const selectedSection = ref(null)
const leftColumnPosts = ref([])
const rightColumnPosts = ref([])
let leftHeight = 0; let rightHeight = 0

onMounted(async () => {
  try {
    const data = await request({ url: '/api/core/sections' })
    sections.value = data
  } catch (err) {}
})

const selectSection = async (section) => {
  selectedSection.value = section; leftColumnPosts.value = []; rightColumnPosts.value = []; leftHeight = 0; rightHeight = 0
  try {
    const data = await request({ url: `/api/core/posts?sectionId=${section.id}&size=20` })
    distributePosts(data.records.map(p => ({
      ...p, imageUrls: typeof p.imageUrls === 'string' ? JSON.parse(p.imageUrls) : (p.imageUrls || [])
    })))
  } catch (err) {}
}

const distributePosts = (newPosts) => {
  newPosts.forEach(post => {
    const match = post.imageUrls[0]?.match(/_w(\d+)_h(\d+)/); const h = match ? 340 * Math.max(0.75, Math.min(1.33, match[2]/match[1])) : 400
    if (leftHeight <= rightHeight) { leftColumnPosts.value.push(post); leftHeight += h }
    else { rightColumnPosts.value.push(post); rightHeight += h }
  })
}

const toggleDark = () => { isDark.value = !isDark.value; uni.setStorageSync('isDark', isDark.value) }
const goToSearch = () => uni.navigateTo({ url: '/pages/search/search' })
const goToDetail = (id) => uni.navigateTo({ url: `/pages/post-detail/post-detail?id=${id}` })
</script>

<style lang="scss" scoped>
.page-container { height: 100vh; display: flex; flex-direction: column; background-color: var(--bg-main); }
.content-scroll { flex: 1; }
.section-grid { display: grid; grid-template-columns: 1fr 1fr; padding: 30rpx; gap: 30rpx; }
.section-card { padding: 40rpx; display: flex; flex-direction: column; align-items: center; justify-content: center; text-align: center; background: var(--surface);
  .section-name { font-size: 32rpx; font-weight: 800; margin-bottom: 8rpx; }
  .section-en { font-size: 20rpx; font-weight: 600; opacity: 0.5; text-transform: uppercase; }
}
.filter-header { margin: 30rpx; padding: 20rpx 30rpx; display: flex; justify-content: space-between; align-items: center; background-color: var(--primary); 
  .filter-title { font-size: 32rpx; font-weight: 800; color: #fff; } .close-btn { padding: 10rpx; color: #fff; }
}
.waterfall { display: flex; padding: 0 20rpx; gap: 20rpx; .column { flex: 1; display: flex; flex-direction: column; gap: 20rpx; } }
</style>
