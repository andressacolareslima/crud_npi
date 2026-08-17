<script setup lang="ts">
import { ref, watch } from 'vue'
import type { Categoria, Livro, LivroFormState } from '../types'
import { requestJson } from '../api'

const props = defineProps<{
  categorias: Categoria[]
  livroParaEdicao: Livro | null
}>()

const emit = defineEmits<{
  (e: 'data-changed'): void
  (e: 'error', message: string): void
}>()

const livroForm = ref<LivroFormState>({ titulo: '', autor: '', preco: '', categoriaId: '' })

watch(
  () => props.livroParaEdicao,
  (novoLivro) => {
    livroForm.value = novoLivro
      ? { ...novoLivro, preco: String(novoLivro.preco), categoriaId: String(novoLivro.categoria.id) }
      : { titulo: '', autor: '', preco: '', categoriaId: '' }
  },
  { deep: true }
)

async function handleLivroSubmit() {
  if (!livroForm.value.titulo.trim() || !livroForm.value.autor.trim() || !livroForm.value.categoriaId) {
    emit('error', 'Preencha título, autor e categoria.')
    return
  }

  const payload = {
    titulo: livroForm.value.titulo.trim(),
    autor: livroForm.value.autor.trim(),
    preco: Number(livroForm.value.preco),
    categoria: { id: Number(livroForm.value.categoriaId) },
  }

  if (Number.isNaN(payload.preco) || payload.preco <= 0) {
    emit('error', 'O preço deve ser maior que zero.')
    return
  }

  try {
    if (livroForm.value.id) {
      await requestJson<Livro>(`/livros/${livroForm.value.id}`, { method: 'PUT', body: JSON.stringify({ id: livroForm.value.id, ...payload }) })
    } else {
      await requestJson<Livro>('/livros', { method: 'POST', body: JSON.stringify(payload) })
    }
    emit('data-changed')
  } catch (err) {
    emit('error', err instanceof Error ? err.message : 'Erro ao salvar livro.')
  }
}

function clearLivroForm() {
  livroForm.value = { titulo: '', autor: '', preco: '', categoriaId: '' }
}
</script>

<template>
  <section class="panel">
    <h2>{{ livroForm.id ? 'Editar livro' : 'Novo livro' }}</h2>
    <form class="stack" @submit.prevent="handleLivroSubmit">
      <label>Título<input v-model="livroForm.titulo" type="text" placeholder="Ex.: O Pequeno Príncipe" /></label>
      <label>Autor<input v-model="livroForm.autor" type="text" placeholder="Ex.: Antoine de Saint-Exupéry" /></label>
      <label>Preço<input v-model="livroForm.preco" type="number" min="0.01" step="0.01" placeholder="49.90" /></label>
      <label>Categoria
        <select v-model="livroForm.categoriaId">
          <option value="">Selecione</option>
          <option v-for="categoria in categorias" :key="categoria.id ?? categoria.nome" :value="String(categoria.id ?? '')">{{ categoria.nome }}</option>
        </select>
      </label>
      <div class="row actions">
        <button type="submit">{{ livroForm.id ? 'Atualizar livro' : 'Salvar livro' }}</button>
        <button v-if="livroForm.id" type="button" class="secondary" @click="clearLivroForm">Cancelar</button>
      </div>
    </form>
  </section>
</template>