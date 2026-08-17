export type Categoria = {
  id?: number
  nome: string
}

export type Livro = {
  id?: number
  titulo: string
  autor: string
  preco: number
  categoria: Categoria
}

export type LivroFormState = {
  id?: number
  titulo: string
  autor: string
  preco: string
  categoriaId: string
}