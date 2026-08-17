export const API_BASE = import.meta.env.VITE_API_BASE_URL || '/api'

export async function requestJson<T>(path: string, options: RequestInit = {}): Promise<T> {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers ?? {}),
    },
    ...options,
  })

  if (!response.ok) {
    const text = await response.text()
    throw new Error(text || 'Erro ao comunicar com a API')
  }

  return response.status === 204 ? (undefined as T) : ((await response.json()) as T)
}