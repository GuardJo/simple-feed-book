"use client"

import {useCallback, useEffect, useRef, useState} from "react";
import {FeedComment, getFeedComments} from "@/lib/feedApiCaller";
import Spinner from "@/components/Spinner";
import FeedCommentItem from "@/components/FeedCommentItem";
import {useQuery} from "@tanstack/react-query";

/**
 * 피드 댓글 스크롤링 목록 컴포넌트
 */
export default function CommentScrollList({feedId}: CommentScrollListProps) {
    const [comments, setComments] = useState<FeedComment[]>([])
    const [page, setPage] = useState(0)

    const observerRef = useRef<IntersectionObserver>(null)
    const loadMoreRef = useRef<HTMLDivElement>(null)

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

    return (
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
    )
}

interface CommentScrollListProps {
    feedId: number,
}