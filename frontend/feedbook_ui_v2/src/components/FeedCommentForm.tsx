"use client"

import {ChangeEvent, FormEvent, useState} from "react";
import {cn} from "@/lib/utils";
import {Send} from "lucide-react";

/**
 * 댓글 입력 폼 컴포넌트
 */
export default function FeedCommentForm({feedId}: FeedCommentFormProps) {
    const [comment, setComment] = useState('')

    const submitNewComment = (event: FormEvent): void => {
        event.preventDefault()
        // TODO API 연동
        console.log(`Save new comment, feedId = ${feedId}, comment = ${comment}`)
    }

    const handleChangeComment = (event: ChangeEvent<HTMLInputElement>): void => {
        setComment(event.target.value)
    }

    return (
        <form className="border-t flex p-3 items-center gap-2" onSubmit={submitNewComment}>
            <input className="flex-1 border rounded-full py-2 px-4 focus:outline-none focus:border-blue-500"
                   type="text" value={comment} onChange={handleChangeComment} placeholder="댓글 입력"/>
            <button type="submit"
                    className={cn('p-2 rounded-full', comment.trim() ? 'text-blue-500 hover:bg-blue-50' : 'text-gray-300')}>
                <Send size={20}/>
            </button>
        </form>
    )
}

interface FeedCommentFormProps {
    feedId: number
}