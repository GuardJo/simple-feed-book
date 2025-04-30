"use client"

import {MessageCircle} from "lucide-react";
import {cn} from "@/lib/utils";
import FeedCommentModal from "@/components/FeedCommentModal";
import {useState} from "react";

/**
 * 피드 댓글 모달 관련 버튼
 */
export default function FeedCommentButton({feedId, disabled = false}: FeedCommentButtonProps) {
    const [isOpen, setIsOpen] = useState(false)

    return (
        <>
            <button className="flex items-center space-x-1" onClick={() => setIsOpen(true)}>
                <MessageCircle size={18}
                               className={cn("transition-colors text-gray-500 hover:text-blu-500", disabled && 'opacity-50')}/>
            </button>
            <FeedCommentModal open={isOpen} onCloseAction={() => setIsOpen(false)} feedId={feedId}/>
        </>
    )
}

interface FeedCommentButtonProps {
    feedId: number,
    disabled?: boolean
}