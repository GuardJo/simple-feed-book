import {http, HttpResponse} from "msw";

const mockUrl = process.env.NEXT_PUBLIC_API_MOCK_URL
const testToken = "TestToken"
const AUTHORIZATION_HEADER_NAME: string = "Authorization"

export const handlers = [
    http.post(`${mockUrl}/api/signup`, () => {
        return HttpResponse.json({
            status: 'OK',
            body: 'SUCCESSES'
        })
    }),
    http.post(`${mockUrl}/api/login`, () => {
        return HttpResponse.json({
            status: 'OK',
            body: testToken
        })
    }),
    http.get(`${mockUrl}/api/feeds`, ({request}) => {
        const token = request.headers.get(AUTHORIZATION_HEADER_NAME)
        const page = new URL(request.url).searchParams.get('page') ?? '0'

        if (token === null) {
            return new HttpResponse("Unauthorized", {
                status: 401
            })
        } else {
            const pageNumber: number = Number(page)
            // 3 페이지가 끝인 데이터
            if (pageNumber < 3) {
                return HttpResponse.json({
                    status: "OK",
                    body: {
                        feeds: [
                            {
                                id: pageNumber + 4,
                                title: "tttt",
                                content: "ccc",
                                author: "Tester2",
                                isOwner: false,
                                isFavorite: false,
                                totalFavorites: 0
                            },
                            {
                                id: pageNumber + 3,
                                title: "title3",
                                content: "test content",
                                author: "Tester1",
                                isOwner: true,
                                isFavorite: false,
                                totalFavorites: 0
                            },
                            {
                                id: pageNumber + 2,
                                title: "title2",
                                content: "test content",
                                author: "Tester1",
                                isOwner: true,
                                isFavorite: false,
                                totalFavorites: 0
                            },
                            {
                                id: pageNumber + 1,
                                title: "title1",
                                content: "test content",
                                author: "Tester1",
                                isOwner: true,
                                isFavorite: true,
                                totalFavorites: 1
                            }
                        ],
                        totalPage: pageNumber,
                    }
                })
            } else {
                return HttpResponse.json({
                    status: "OK",
                    body: {
                        feeds: [],
                        totalPage: 1,
                    }
                })
            }
        }
    }),
    http.get(`${mockUrl}/api/feeds/me`, ({request}) => {
        const token = request.headers.get(AUTHORIZATION_HEADER_NAME)
        const page = new URL(request.url).searchParams.get('page') ?? '0'

        if (token === null) {
            return new HttpResponse("Unauthorized", {
                status: 401
            })
        } else {
            const pageNumber: number = Number(page)
            // 3 페이지가 끝인 데이터
            if (pageNumber < 1) {
                return HttpResponse.json({
                    status: "OK",
                    body: {
                        feeds: [
                            {
                                id: pageNumber + 2,
                                title: "title2",
                                content: "test content",
                                author: "Tester1",
                                isOwner: true,
                                isFavorite: false,
                                totalFavorites: 0
                            },
                            {
                                id: pageNumber + 1,
                                title: "title1",
                                content: "test content",
                                author: "Tester1",
                                isOwner: true,
                                isFavorite: true,
                                totalFavorites: 1
                            }
                        ],
                        totalPage: pageNumber,
                    }
                })
            } else {
                return HttpResponse.json({
                    status: "OK",
                    body: {
                        feeds: [],
                        totalPage: 1,
                    }
                })
            }
        }
    }),
    http.delete(`${mockUrl}/api/feeds/:feedId`, ({request}) => {
        const token = request.headers.get(AUTHORIZATION_HEADER_NAME)

        if (token === null) {
            return new HttpResponse("Unauthorized", {
                status: 401
            })
        }

        return HttpResponse.json({
            status: "OK",
            body: "SUCCESSES"
        })
    })
]