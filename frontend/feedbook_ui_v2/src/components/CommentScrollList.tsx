"use client"

import {useCallback, useEffect, useRef, useState} from "react";
import {FeedComment} from "@/lib/feedApiCaller";
import Spinner from "@/components/Spinner";
import FeedCommentItem from "@/components/FeedCommentItem";

/**
 * 피드 댓글 스크롤링 목록 컴포넌트
 */
export default function CommentScrollList({feedId}: CommentScrollListProps) {
    const [comments, setComments] = useState<FeedComment[]>([])
    const [loading, setLoading] = useState(false)
    const [page, setPage] = useState(0)

    const observerRef = useRef<IntersectionObserver>(null)
    const loadMoreRef = useRef<HTMLDivElement>(null)

    const observerCallback = useCallback((entries: IntersectionObserverEntry[]) => {
        const onLoadMore = (): void => {
            if (loading) {
                return
            }

            console.log(`Loading more comments(feedId = ${feedId})...`)

            // TODO API 연동
            const newComments: FeedComment[] = [{
                id: page + 1,
                author: 'Tester22',
                createTime: new Date().toString(),
                content: 'comment~'
            }, {
                id: page + 2,
                author: 'Tester223',
                createTime: new Date().toString(),
                content: 'comment~'
            }, {
                id: page + 3,
                author: 'Tester224',
                createTime: new Date().toString(),
                content: 'comment~'
            }]

            setLoading(false)

            if (newComments.length > 0) {
                setComments((prevState: FeedComment[]) => [...prevState, ...newComments])
                setPage((prevState: number) => prevState + 1)
            }
        }

        const [entry] = entries
        if (entry.isIntersecting && !loading) {
            console.log('Intersection detected')
            onLoadMore()
        }
    }, [loading, page])

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
            {comments.map((comment) => (
                <FeedCommentItem key={comment.id} comment={comment}/>
            ))}
            <div ref={loadMoreRef} className="py-8 flex justify-center">
                {loading ? <Spinner/> : <div className="h-10"/>}
            </div>
        </div>
    )
}

interface CommentScrollListProps {
    feedId: number,
}