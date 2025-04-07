"use client"

import {ChangeEvent, FormEvent} from "react";
import {cn} from "@/lib/utils";
import {Send} from "lucide-react";

/**
 * 댓글 입력 폼 컴포넌트
 */
export default function FeedCommentForm({value, onChangeAction, onSubmitAction}: FeedCommentFormProps) {

    return (
        <form className="border-t flex p-3 items-center gap-2" onSubmit={onSubmitAction}>
            <input className="flex-1 border rounded-full py-2 px-4 focus:outline-none focus:border-blue-500"
                   type="text" value={value} onChange={onChangeAction}
                   placeholder="댓글 입력"/>
            <button type="submit"
                    className={cn('p-2 rounded-full', value.trim() ? 'text-blue-500 hover:bg-blue-50' : 'text-gray-300')}>
                <Send size={20}/>
            </button>
        </form>
    )
}

interface FeedCommentFormProps {
    value: string
    onChangeAction: (e: ChangeEvent<HTMLInputElement>) => void
    onSubmitAction: (e: FormEvent) => void
}