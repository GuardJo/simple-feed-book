"use client"

import {ReactNode, useState} from "react";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import {ReactQueryDevtools} from "@tanstack/react-query-devtools";

export default function QueryProvider({children}: QueryProviderProps) {
    const [queryClient] = useState(new QueryClient({
        defaultOptions: {
            queries: {
                staleTime: 1000 * 60
            }
        }
    }))

    return (
        <QueryClientProvider client={queryClient}>
            {children}
            <ReactQueryDevtools/>
        </QueryClientProvider>
    )
}

interface QueryProviderProps {
    children: ReactNode
}