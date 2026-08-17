<script setup lang="ts">
import { ref } from 'vue'
import type { Categoria } from '../types'
import { requestJson } from '../api'

const props = defineProps<{
  categorias: Categoria[]
  isLoading: boolean
}>()

const emit = defineEmits<{
  (e: 'data-changed'): void
  (e: 'error', message: string): void
}>()

const categoriaNome = ref('')
const editingCategoriaId = ref<number | null>(null)

async function handleCategoriaSubmit() {
  if (!categoriaNome.value.trim()) {
    emit('error', 'Informe o nome da categoria.')
    return
  }

  try {
    const payload = { nome: categoriaNome.value.trim() }
    if (editingCategoriaId.value) {
      await requestJson<Categoria>(`/categorias/${editingCategoriaId.value}`, {
        method: 'PUT',
        body: JSON.stringify({ id: editingCategoriaId.value, ...payload }),
      })
    } else {
      await requestJson<Categoria>('/categorias', {
        method: 'POST',
        body: JSON.stringify(payload),
      })
    }
    cancelCategoriaEdit()
    emit('data-changed')
  } catch (err) {
    emit('error', err instanceof Error ? err.message : 'Erro ao salvar categoria.')
  }
}

async function handleDeleteCategoria(id?: number) {
  if (!id) return
  try {
    await requestJson(`/categorias/${id}`, { method: 'DELETE' })
    emit('data-changed')
  } catch (err) {
    emit('error', err instanceof Error ? err.message : 'Erro ao excluir categoria.')
  }
}

function editCategoria(cat: Categoria) {
  editingCategoriaId.value = cat.id ?? null
  categoriaNome.value = cat.nome
}

function cancelCategoriaEdit() {
  editingCategoriaId.value = null
  categoriaNome.value = ''
}
</script>

<template>
  <section class="panel">
    <h2>{{ editingCategoriaId ? 'Editar categoria' : 'Nova categoria' }}</h2>
    <form class="stack" @submit.prevent="handleCategoriaSubmit">
      <label>Nome<input v-model="categoriaNome" type="text" placeholder="Ex.: Romance" /></label>
      <div class="row actions">
        <button :disabled="isLoading" type="submit">{{ editingCategoriaId ? 'Atualizar' : 'Salvar' }}</button>
        <button v-if="editingCategoriaId" :disabled="isLoading" type="button" class="secondary" @click="cancelCategoriaEdit">Cancelar</button>
      </div>
    </form>

    <div class="list">
      <p v-if="categorias.length === 0" class="empty">Nenhuma categoria cadastrada.</p>
      <transition-group name="list" tag="div">
        <div v-for="categoria in categorias" :key="categoria.id ?? categoria.nome" class="list-item">
          <span>{{ categoria.nome }}</span>
          <div class="row">
            <button :disabled="isLoading" type="button" class="secondary" @click="editCategoria(categoria)">Editar</button>
            <button :disabled="isLoading" type="button" class="danger" @click="handleDeleteCategoria(categoria.id)">Excluir</button>
          </div>
        </div>
      </transition-group>
    </div>
  </section>
</template>