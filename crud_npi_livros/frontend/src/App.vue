<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { Categoria, Livro } from './types'
import { requestJson } from './api'
import CategoriaManager from './components/CategoriaManager.vue'
import LivroForm from './components/LivroForm.vue'
import LivroList from './components/LivroList.vue'

const categorias = ref<Categoria[]>([])
const livros = ref<Livro[]>([])
const livroParaEdicao = ref<Livro | null>(null)
const isLoading = ref(true)
const error = ref<string | null>(null)

async function loadData() {
  try {
    isLoading.value = true
    const [categoriasResponse, livrosResponse] = await Promise.all([
      requestJson<Categoria[]>('/categorias'),
      requestJson<Livro[]>('/livros'),
    ])

    categorias.value = categoriasResponse
    livros.value = livrosResponse
    error.value = null
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Não foi possível carregar os dados.'
  } finally {
    isLoading.value = false
  }
}

function handleDataChanged() {
  livroParaEdicao.value = null
  void loadData()
}

function handleError(message: string) {
  error.value = message
}

onMounted(() => {
  void loadData()
})
</script>

<template>
  <main class="page-shell">
    <header class="topbar">
      <div>
        <p class="eyebrow">Inserir informações sobre livros e adicionar categorias</p>
        <h1>Gestão de categorias e livros</h1>
      </div>
    </header>

    <div v-if="error" class="alert">{{ error }}</div>

    <transition name="fade">
      <div v-if="isLoading" class="loading-overlay" aria-hidden="true">
        <div class="spinner" role="status" aria-label="Carregando"></div>
      </div>
    </transition>

    <transition name="fade" mode="out-in">
      <div v-if="!isLoading" key="main-content">
        <div class="content-root">
          <div class="grid">
            <CategoriaManager :categorias="categorias" :is-loading="isLoading" @data-changed="handleDataChanged" @error="handleError" />
            <LivroForm :categorias="categorias" :livro-para-edicao="livroParaEdicao" @data-changed="handleDataChanged" @error="handleError" />
          </div>
        </div>

        <LivroList
          :livros="livros"
          @edit="livroParaEdicao = $event"
          @data-changed="handleDataChanged"
          @error="handleError"
        />
      </div>
    </transition>
  </main>
</template>
