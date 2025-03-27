"use client"

import {ReactNode, useEffect, useState} from "react";
import {initMsw} from "@/mocks";

export default function MswProvider({children}: MswProviderProps) {
    const [mswReady, setMswReady] = useState(false)

    useEffect(() => {
        if (!mswReady) {
            initMsw()
                .then(() => setMswReady(true))
        }
    }, [mswReady])

    return <>{children}</>
}

interface MswProviderProps {
    children: ReactNode
}