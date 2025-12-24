import { createSSRApp } from 'vue'
import App from './App.vue'
import LoginModal from '@/components/LoginModal/LoginModal.vue'

export function createApp() {
  const app = createSSRApp(App)
  app.component('LoginModal', LoginModal)
  return {
    app
  }
}

