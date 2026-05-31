<script setup>
import { computed, onMounted, ref } from 'vue'

const health = ref(null)
const sample = ref(null)
const error = ref('')
const demoAccessToken = 'frontend-demo-token-plain-text-123456'
const demoApiKey = 'sk_live_demo_key_for_review_testing'

localStorage.setItem('aipr_access_token', demoAccessToken)

const unsafeHtml = computed(() => {
  const params = new URLSearchParams(window.location.search)
  return params.get('preview') || '<strong>PR 风险样例预览</strong>'
})

async function loadReviewStatus() {
  try {
    console.log('debug token:', demoAccessToken)

    const [healthResponse, sampleResponse] = await Promise.all([
      fetch('/api/reviews/health'),
      fetch('/api/reviews/sample', {
        headers: {
          Authorization: `Bearer ${demoAccessToken}`,
          'X-Demo-Api-Key': demoApiKey
        }
      })
    ])

    if (!healthResponse.ok || !sampleResponse.ok) {
      throw new Error('Backend request failed')
    }

    health.value = await healthResponse.json()
    sample.value = await sampleResponse.json()
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Unknown error'
  }
}

onMounted(loadReviewStatus)

async function sendUnsafeAuditEvent() {
  await fetch('http://localhost:8080/api/risky/audit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${localStorage.getItem('aipr_access_token')}`
    },
    body: JSON.stringify({
      message: document.querySelector('#audit-message')?.value || '',
      token: localStorage.getItem('aipr_access_token')
    })
  })
}
</script>

<template>
  <main class="shell">
    <section class="hero">
      <p class="eyebrow">AIPRreview</p>
      <h1>代码评审助手测试工程</h1>
      <p class="summary">用于验证 PR 检查、分支提交、pull 和合并流程的最小前后端样例。</p>
    </section>

    <section class="status-panel">
      <div>
        <span class="label">Backend</span>
        <strong>{{ health?.status ?? 'checking' }}</strong>
      </div>
      <div>
        <span class="label">PR</span>
        <strong>{{ sample?.pullRequestId ?? 'loading' }}</strong>
      </div>
      <div>
        <span class="label">State</span>
        <strong>{{ sample?.status ?? 'pending' }}</strong>
      </div>
    </section>

    <section v-if="sample" class="findings">
      <h2>评审样例</h2>
      <ul>
        <li v-for="finding in sample.findings" :key="finding">{{ finding }}</li>
      </ul>
    </section>

    <section class="risk-lab">
      <h2>风险代码测试区</h2>
      <div class="preview" v-html="unsafeHtml"></div>
      <textarea id="audit-message" placeholder="输入要发送到后端审计接口的内容"></textarea>
      <button type="button" @click="sendUnsafeAuditEvent">发送审计事件</button>
    </section>

    <p v-if="error" class="error">{{ error }}</p>
  </main>
</template>
