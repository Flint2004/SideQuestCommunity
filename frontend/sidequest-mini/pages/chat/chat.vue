<template>
  <view :class="{ 'dark': isDark }" class="page-container">
    <!-- 固定页头：移出 scroll-view -->
    <view class="fixed-header">
      <view class="status-bar" :style="{ height: statusBarHeight + 'px' }" />
      <view class="nav-bar">
        <view class="back-btn brutal-btn" @click="goBack">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3">
            <polyline points="15 18 9 12 15 6"></polyline>
          </svg>
        </view>
        <text class="title">{{ recipientName }}</text>
      </view>
    </view>
    
    <!-- 消息滚动区：flex: 1 确保可滑动 -->
    <scroll-view 
      scroll-y 
      class="chat-scroll" 
      :scroll-into-view="lastMessageId"
      :scroll-with-animation="true"
    >
      <view class="msg-list">
        <view v-for="msg in messages" :key="msg.id" :id="'msg-' + msg.id" class="msg-row" :class="{ 'me': msg.senderId === myId }">
          <image v-if="msg.senderId !== myId" :src="recipientAvatar" class="avatar brutal-card" />
          <view class="bubble-container">
            <view class="msg-bubble brutal-card" :class="{ 'primary': msg.senderId === myId }">
              <text class="msg-text">{{ msg.content }}</text>
            </view>
          </view>
          <image v-if="msg.senderId === myId" :src="myAvatar" class="avatar brutal-card" />
        </view>
        <view id="bottom-anchor" style="height: 20rpx;" />
      </view>
    </scroll-view>
    
    <view class="chat-input-area safe-area-bottom brutal-card">
      <input v-model="inputMsg" class="input-box brutal-btn" placeholder="输入消息..." @confirm="sendMsg" />
      <view class="send-btn brutal-btn primary" @click="sendMsg"><text>发送</text></view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import request from '@/utils/request'

const isDark = ref(uni.getStorageSync('isDark') || false); const statusBarHeight = ref(uni.getSystemInfoSync().statusBarHeight)
const myId = 1; const myAvatar = 'https://picsum.photos/200/200?random=10'
const recipientName = ref('野兽派小秘书'); const recipientAvatar = ref('https://picsum.photos/100/100?random=20')
const messages = ref([]); const inputMsg = ref(''); const lastMessageId = ref('')

onMounted(async () => {
  const pages = getCurrentPages(); const id = pages[pages.length - 1].options.roomId || 1
  messages.value = await request({ url: `/api/chat/rooms/${id}/messages` })
  scrollToBottom()
})

const sendMsg = () => {
  if (!inputMsg.value) return
  messages.value.push({ id: Date.now(), senderId: myId, content: inputMsg.value })
  inputMsg.value = ''; scrollToBottom()
}
const scrollToBottom = () => nextTick(() => lastMessageId.value = 'bottom-anchor')
const goBack = () => uni.navigateBack()
</script>

<style lang="scss" scoped>
.page-container { height: 100vh; display: flex; flex-direction: column; background: var(--bg-main); overflow: hidden; }

.fixed-header { 
  background: var(--surface); border-bottom: 4rpx solid #000; z-index: 1000;
  .nav-bar { height: 100rpx; display: flex; align-items: center; padding: 0 30rpx; gap: 30rpx;
    .back-btn { width: 70rpx; height: 70rpx; border-radius: 16rpx; }
    .title { font-size: 32rpx; font-weight: 800; color: var(--text-main); }
  }
}

.chat-scroll { flex: 1; height: 0; // 关键：确保 flex 布局下的滚动生效
  .msg-list { padding: 40rpx 24rpx 160rpx; }
}

.msg-row { display: flex; gap: 20rpx; margin-bottom: 48rpx; align-items: flex-start; &.me { justify-content: flex-end; } }
.avatar { width: 88rpx; height: 88rpx; border-radius: 24rpx; flex-shrink: 0; box-shadow: 4rpx 4rpx 0 #000; }
.bubble-container { max-width: 65%; 
  .msg-bubble { padding: 24rpx 28rpx; background: var(--surface); border-radius: 28rpx; box-shadow: 6rpx 6rpx 0 #000;
    .msg-text { font-size: 28rpx; line-height: 1.5; color: var(--text-main); word-break: break-all; }
    &.primary { background: var(--primary); box-shadow: -6rpx 6rpx 0 #000; .msg-text { color: #fff !important; } }
  }
}

.chat-input-area { position: fixed; bottom: 0; left: 0; right: 0; height: 140rpx; background: var(--surface); display: flex; align-items: center; padding: 0 30rpx calc(env(safe-area-inset-bottom) + 10rpx); gap: 20rpx; border-radius: 48rpx 48rpx 0 0; border-top: 4rpx solid #000; z-index: 100;
  .input-box { flex: 1; height: 88rpx; padding-left: 30rpx; font-size: 28rpx; background: var(--bg-main); border: 2rpx solid #000; } 
  .send-btn { width: 140rpx; height: 88rpx; font-size: 30rpx; flex-shrink: 0; } 
}
</style>
