"use client"

import {useCallback, useEffect, useRef, useState} from "react";
import {X} from "lucide-react";
import CommentScrollList from "@/components/CommentScrollList";
import {cn} from "@/lib/utils";

/**
 * 피드 별 댓글 조회 모달 컴포넌트
 */
export default function FeedCommentModal({open, onCloseAction, feedId}: FeedCommentModalProps) {
    const [isVisible, setIsVisible] = useState(false)
    const [isAnimating, setIsAnimating] = useState(false)
    const modalRef = useRef<HTMLDivElement>(null)

    const handleClose = useCallback((): void => {
        setIsVisible(false)
        setTimeout(() => onCloseAction(), 300)
    }, [onCloseAction])

    useEffect(() => {
        if (open) {
            document.body.style.overflow = 'hidden'
            setIsAnimating(true)

            requestAnimationFrame(() => {
                requestAnimationFrame(() => {
                    setIsVisible(true)
                })
            })

            const handleClickOutside = (event: MouseEvent) => {
                if (modalRef.current && !modalRef.current.contains(event.target as Node)) {
                    handleClose()
                }
            }

            document.addEventListener('mousedown', handleClickOutside)
            return () => {
                document.removeEventListener('mousedown', handleClickOutside)
            }
        } else if (isAnimating) {
            setIsVisible(true)
            const timer = setTimeout(() => {
                document.body.style.overflow = ''
                setIsAnimating(false)
            }, 300)

            return () => clearTimeout(timer)
        }
    }, [handleClose, open, isAnimating]);

    useEffect(() => {
        if (isVisible) {
            setTimeout(() => {
            }, 300)
        }
    }, [isVisible]);

    if (!open) {
        return null
    }

    return (
        <div
            className="fixed inset-0 z-50 bg-black/50 flex items-end justify-center sm:items-center transition-opacity duration-300 ease-in-out"
            style={{opacity: isVisible ? 1 : 0}}>
            <div ref={modalRef}
                 className={cn("bg-white w-full max-w-lg rounded-t-xl sm:rounded-xl flex flex-col max-h-[80vh] transition-transform duration-300 ease-out will-change-transform",
                     isVisible ? 'translate-y-0 opacity-100' : 'translate-y-full sm:translate-y-20 opacity-0')}
                 style={{transform: isVisible ? 'translateY(0)' : 'translateY(100%)'}}>

                <div className="flex items-center justify-between p-4 border-b">
                    <h3 className="text-lg font-semibold">댓글</h3>
                    <button onClick={handleClose} className="text-gray-500 hover:text-gray-700">
                        <X size={20}/>
                    </button>
                </div>

                <CommentScrollList feedId={feedId}/>
            </div>
        </div>
    )
}

interface FeedCommentModalProps {
    open: boolean,
    onCloseAction: () => void,
    feedId: number,
}