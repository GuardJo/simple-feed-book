import type {Preview} from '@storybook/react'
import "../src/app/globals.css"
import MswProvider from "../src/containers/MswProvider";
import QueryProvider from "../src/containers/QueryProvider";
import {initialize, mswLoader} from "msw-storybook-addon";
import {handlers} from "../src/mocks/handlers";

initialize()

const preview: Preview = {
    parameters: {
        controls: {
            matchers: {
                color: /(background|color)$/i,
                date: /Date$/i,
            },
        },
        nextjs: {
            appDirectory: true,
        },
        docs: {
            story: {
                inline: false,
                iframeHeight: 400
            }
        },
        msw: {
            handlers: [...handlers]
        }
    },
    decorators: [
        (Story) => {
            return (
                <MswProvider>
                    <QueryProvider>
                        <Story/>
                    </QueryProvider>
                </MswProvider>
            )
        }
    ],
    loaders: [mswLoader],
};

export default preview;