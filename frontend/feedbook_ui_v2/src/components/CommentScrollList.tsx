"use client"

import {ChangeEvent, FormEvent, useCallback, useEffect, useRef, useState} from "react";
import {FeedComment, FeedCommentCreateRequest, getFeedComments, saveNewFeedComment} from "@/lib/feedApiCaller";
import Spinner from "@/components/Spinner";
import FeedCommentItem from "@/components/FeedCommentItem";
import {QueryClient, useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import FeedCommentForm from "@/components/FeedCommentForm";

/**
 * 피드 댓글 스크롤링 목록 컴포넌트
 */
export default function CommentScrollList({feedId}: CommentScrollListProps) {
    const [comments, setComments] = useState<FeedComment[]>([])
    const [page, setPage] = useState(0)
    const [comment, setComment] = useState('')

    const observerRef = useRef<IntersectionObserver>(null)
    const loadMoreRef = useRef<HTMLDivElement>(null)

    const queryClient: QueryClient = useQueryClient()

    const createCommentMutation = useMutation({
        mutationKey: ['saveNewFeedComment', {feedId}],
        mutationFn: (request: FeedCommentCreateRequest) => saveNewFeedComment(request),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: ["getFeedComments"],
                refetchType: "all"
            })

            setComments((prevState) => {
                const newComments: FeedComment[] = [
                    {
                        id: (comments.length + 1) * -1,
                        author: 'Me',
                        content: comment,
                        createTime: 'Now'
                    }
                ]
                return [...prevState, ...newComments]
            })
            setComment('')
        },
        onError: error => {
            console.log(`Failed create comment, feedId = ${feedId}, cause = ${error.message}`)
            window.alert(error.message)
        }
    })

    const {data, isLoading, isError, error} = useQuery({
        queryKey: ['getFeedComments', {feedId, page}],
        queryFn: () => getFeedComments(feedId, page)
    })

    const observerCallback = useCallback((entries: IntersectionObserverEntry[]) => {
        const onLoadMore = (): void => {
            if (isLoading) {
                return
            }

            console.log(`Loading more comments...`)

            if (isError) {
                console.log(`getFeedContents Error : ${error.message}`)
            } else {
                if (data !== undefined) {
                    const newComments: FeedComment[] = data.body.comments

                    if (newComments.length > 0) {
                        setComments((prevState: FeedComment[]) => [...prevState, ...newComments])
                        setPage((prevState: number) => prevState + 1)
                    }
                }
            }
        }

        const [entry] = entries
        if (entry.isIntersecting && !isLoading) {
            console.log('Intersection detected')
            onLoadMore()
        }
    }, [data, isError, error, isLoading])

    useEffect(() => {
        const currentRef = loadMoreRef.current

        if (currentRef) {
            observerRef.current = new IntersectionObserver(observerCallback, {
                root: null,
                rootMargin: '100px',
                threshold: 0.1
            })

            observerRef.current.observe(currentRef)
            console.log('Observe set up')
        }

        return () => {
            if (observerRef.current && currentRef) {
                observerRef.current.unobserve(currentRef)
            }
        }
    }, [observerCallback]);

    useEffect(() => {
        console.log(`Updated page = ${page}`)
    }, [page]);

    const submitNewComment = (event: FormEvent): void => {
        event.preventDefault()
        createCommentMutation.mutate({
            feedId: feedId,
            content: comment,
        })
    }

    const handleChangeComment = (event: ChangeEvent<HTMLInputElement>): void => {
        setComment(event.target.value)
    }

    return (
        <>
            <div className="flex-1 overflow-y-auto p-4 space-y-4">
                {comments.length === 0 ? (
                    <p className="text-cneter text-gray-500 py-8">댓글이 없습니다.</p>
                ) : (
                    <>
                        {comments.map((comment) => (
                            <FeedCommentItem key={comment.id} comment={comment}/>
                        ))}
                    </>
                )}
                <div ref={loadMoreRef} className="py-8 flex justify-center">
                    {isLoading ? <Spinner/> : <div className="h-10"/>}
                </div>
            </div>
            <FeedCommentForm value={comment} onChangeAction={handleChangeComment} onSubmitAction={submitNewComment}/>
        </>
    )
}

interface CommentScrollListProps {
    feedId: number,
}