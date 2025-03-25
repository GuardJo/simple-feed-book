"use client"

import {cn} from "@/lib/utils";
import {HTMLInputTypeAttribute, useState} from "react";

/**
 * Custom Form Input : Focus 상태 시 label 애니메이션 발생
 */
export default function FocusingInput({id, labelName, type, value = ""}: FocusingInputProps) {
    const [focusedLabel, setFocusedLabel] = useState(false)
    const [inputValue, setInputValue] = useState(value)

    return (
        <div className="relative">
            <label htmlFor={id} className={
                cn(
                    "absolute left-0 transition-all duration-200",
                    focusedLabel || inputValue ? "-top-6 text-sm text-blue-500" : "top-2 text-gray-500",
                )
            }>
                {labelName}
            </label>
            <input id={id} type={type}
                   onChange={(e) => setInputValue(e.target.value)}
                   onFocus={() => setFocusedLabel(true)}
                   onBlur={() => setFocusedLabel(false)}
                   value={inputValue}
                   className="w-full border-b border-gray-300 bg-transparent py-2 focus:border-blue-500 focus:outline-none"
                   required/>
        </div>
    )
}

interface FocusingInputProps {
    /** Input ID */
    id: string,
    /** Label DisplayName */
    labelName: string,
    /** Input Type */
    type: HTMLInputTypeAttribute,
    /** Input Value */
    value?: string,
}