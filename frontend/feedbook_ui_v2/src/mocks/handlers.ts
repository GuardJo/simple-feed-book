import {http, HttpResponse} from "msw";
import {FeedComment} from "@/lib/feedApiCaller";
import {Notification} from "@/lib/notificationApiCaller";

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
    }),
    http.put(`${mockUrl}/api/feeds/favorites/:feedId`, ({request}) => {
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
    }),
    http.patch(`${mockUrl}/api/feeds`, ({request}) => {
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
    }),
    http.get(`${mockUrl}/api/feeds/:feedId/comments`, ({request, params}) => {
        const token = request.headers.get(AUTHORIZATION_HEADER_NAME)
        const page = new URL(request.url).searchParams.get('page') ?? '0'
        const feedId = params.feedId

        if (token === null) {
            return new HttpResponse("Unauthorized", {
                status: 401
            })
        } else {
            const feedIdNumber: number = Number(feedId)
            const pageNumber: number = Number(page)
            if (feedIdNumber > 1 || pageNumber > 3) {
                return HttpResponse.json({
                    status: 'OK',
                    body: {
                        totalPage: 1,
                        comments: [],
                    }
                })
            } else {
                const feedComments: FeedComment[] = []

                for (let i = 0; i < 10; i++) {
                    feedComments.push({
                        id: i,
                        author: `Tester${i}`,
                        createTime: new Date().toString(),
                        content: 'Test comment',
                    })
                }

                return HttpResponse.json({
                    status: "OK",
                    body: {
                        totalPage: 1,
                        comments: feedComments
                    }
                })
            }
        }
    }),
    http.post(`${mockUrl}/api/feeds/:feedId/comments`, ({request}) => {
        const token = request.headers.get(AUTHORIZATION_HEADER_NAME)

        if (token === null) {
            return new HttpResponse("Unauthorized", {
                status: 401
            })
        } else {
            return HttpResponse.json({
                status: 'OK',
                body: 'SUCCESSES'
            })
        }
    }),
    http.post(`${mockUrl}/api/feeds`, ({request}) => {
        const token = request.headers.get(AUTHORIZATION_HEADER_NAME)

        if (token === null) {
            return new HttpResponse("Unauthorized", {
                status: 401
            })
        } else {
            return HttpResponse.json({
                status: 'OK',
                body: 'SUCCESSES'
            })
        }
    }),
    http.get(`${mockUrl}/api/accounts/alarms`, ({request}) => {
        const token = request.headers.get(AUTHORIZATION_HEADER_NAME)
        const page = new URL(request.url).searchParams.get('page') ?? '0'

        if (token === null) {
            return new HttpResponse("Unauthorized", {
                status: 401
            })
        } else {
            const pageNumber: number = Number(page)

            if (pageNumber >= 2) {
                return HttpResponse.json({
                    status: 'OK',
                    body: {
                        totalSize: 13,
                        pageNumber: page,
                        feedAlarms: []
                    }
                })
            } else {
                const notis: Notification[] = []
                for (let i = 0; i < 10; i++) {
                    notis.push({alarmText: `Test_${page}_${i}`, alarmTime: '1일'})
                }

                return HttpResponse.json({
                    status: "OK",
                    body: {
                        totalSize: 30,
                        pageNumber: page,
                        feedAlarms: notis,
                    }
                })
            }
        }
    }),
    http.get(`${mockUrl}/api/accounts/alarms/sub`, ({request}) => {
        const token = request.headers.get(AUTHORIZATION_HEADER_NAME)

        if (token === null) {
            return new HttpResponse("Unauthorized", {
                status: 401
            })
        } else {
            const stream = new ReadableStream({
                start() {
                    // mocking event-stream
                }
            })

            return new HttpResponse(stream, {
                headers: {
                    'Content-Type': 'text/event-stream',
                }
            })
        }
    }),
    http.get(`${mockUrl}/api/auth`, ({request}) => {
        const token = request.headers.get(AUTHORIZATION_HEADER_NAME)

        if (token === null) {
            return new HttpResponse("Unauthorized", {
                status: 401
            })
        } else {
            return HttpResponse.json({
                status: 'OK',
                body: 'SUCCESSES'
            })
        }
    })
]