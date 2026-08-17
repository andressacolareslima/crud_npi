<script setup lang="ts">
import type { Livro } from '../types'
import { requestJson } from '../api'

defineProps<{
  livros: Livro[]
}>()

const emit = defineEmits<{
  (e: 'edit', livro: Livro): void
  (e: 'data-changed'): void
  (e: 'error', message: string): void
}>()

async function handleDeleteLivro(id?: number) {
  if (!id) return
  try {
    await requestJson(`/livros/${id}`, { method: 'DELETE' })
    emit('data-changed')
  } catch (err) {
    emit('error', err instanceof Error ? err.message : 'Erro ao excluir livro.')
  }
}
</script>

<template>
  <section class="panel table-panel">
    <h2>Livros cadastrados</h2>
    <p v-if="livros.length === 0" class="empty">Nenhum livro cadastrado.</p>
    <div v-else class="table-wrap">
      <table>
        <thead><tr><th>Título</th><th>Autor</th><th>Preço</th><th>Categoria</th><th>Ações</th></tr></thead>
        <tbody>
          <tr v-for="livro in livros" :key="livro.id ?? `${livro.titulo}-${livro.autor}`">
            <td>{{ livro.titulo }}</td><td>{{ livro.autor }}</td>
            <td>{{ new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(livro.preco) }}</td>
            <td>{{ livro.categoria?.nome ?? 'Sem categoria' }}</td>
            <td class="row actions-cell">
              <button type="button" class="secondary" @click="$emit('edit', livro)">Editar</button>
              <button type="button" class="danger" @click="handleDeleteLivro(livro.id)">Excluir</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>