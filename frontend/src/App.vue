<script setup>
import { onMounted, ref } from 'vue'

const health = ref(null)
const sample = ref(null)
const error = ref('')

async function loadReviewStatus() {
  try {
    const [healthResponse, sampleResponse] = await Promise.all([
      fetch('/api/reviews/health'),
      fetch('/api/reviews/sample')
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

    <p v-if="error" class="error">{{ error }}</p>
  </main>
</template>
