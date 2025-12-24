import { reactive } from 'vue'

export const bus = reactive({
  showLogin: false,
  unreadCount: 0,
  
  openLogin() {
    this.showLogin = true
  },
  closeLogin() {
    this.showLogin = false
  },
  
  updateUnreadCount(count) {
    this.unreadCount = count
  }
})
