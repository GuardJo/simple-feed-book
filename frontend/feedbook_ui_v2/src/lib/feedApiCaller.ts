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

export async function getFeeds(page: number): Promise<BaseResponse<FeedPage>> {
    let token: string = ''
    try {
        token = getAccessToken()
    } catch (error) {
        console.log(error)
        window.location.replace('/login')
    }

    const response: Response = await fetch(`${baseUrl}/api/feeds?page=${page}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })

    return validateResponse(response)
}