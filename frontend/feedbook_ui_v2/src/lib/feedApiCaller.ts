import {getAccessToken, validateResponse} from "@/lib/utils";
import {BaseResponse} from "@/lib/models";

const baseUrl = process.env.NEXT_PUBLIC_API_SERVER_URL

export type Feed = {
    id: number,
    title: string,
    content: string,
    author: string,
    isOwner: boolean,
    isFavorite: boolean,
    totalFavorites: number,
}

export type FeedPage = {
    feeds: Feed[],
    totalPage: number,
}

export type FeedUpdateRequest = {
    feedId: number,
    title: string,
    content: string,
}

export type FeedComment = {
    id: number,
    author: string,
    createTime: string,
    content: string
}

const initHeaders = (): HeadersInit => {
    let token: string = ''
    try {
        token = getAccessToken()
    } catch (error) {
        console.log(error)
        window.location.replace('/login')
    }

    return {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    }
}

/**
 * 저장된 피드 목록 조회
 * @param page 불러올 페이지 Number (default = 0)
 * @param onlyMe 본인 작성 피드 필터링 여부
 */
export async function getFeeds(page: number, onlyMe: boolean): Promise<BaseResponse<FeedPage>> {
    const apiUrl = onlyMe ? '/api/feeds/me' : '/api/feeds'
    const response: Response = await fetch(`${baseUrl}${apiUrl}?page=${page}`, {
        method: 'GET',
        headers: {
            ...initHeaders()
        }
    })

    return validateResponse(response)
}

/**
 * 특정 피드 삭제 요청
 * @param feedId 삭제할 피드 식별키
 */
export async function removeFeed(feedId: number): Promise<BaseResponse<string>> {
    const response: Response = await fetch(`${baseUrl}/api/feeds/${feedId}`, {
        method: 'DELETE',
        headers: {
            ...initHeaders()
        }
    })

    return validateResponse(response)
}

/**
 * 특정 피드의 좋아요 갱신
 * @param feedId 갱신할 피드 식별키
 */
export async function updateFavorite(feedId: number): Promise<BaseResponse<string>> {
    const response: Response = await fetch(`${baseUrl}/api/feeds/favorites/${feedId}`, {
        method: 'PUT',
        headers: {
            ...initHeaders()
        }
    })

    return validateResponse(response)
}

/**
 * 특정 피드의 제목/내용 갱신
 * @param request feedId, title, content
 */
export async function updateFeed(request: FeedUpdateRequest): Promise<BaseResponse<string>> {
    const response: Response = await fetch(`${baseUrl}/api/feeds`, {
        method: 'PATCH',
        headers: {
            ...initHeaders()
        },
        body: JSON.stringify(request)
    })

    return validateResponse(response)
}