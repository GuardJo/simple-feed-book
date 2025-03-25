import type {Preview} from '@storybook/react'
import "../src/app/globals.css"

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
        }
    },
    tags: ["autodocs"]
};

export default preview;