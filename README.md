# Multi Threaded File Searcher
## CLI Arguments
##### Indexing
- `-i`: Builds index for the files in the current working directory and saves it in `mtfs-index.txt`
- `-r <PATH>`: Builds index for the directory passed as `<PATH>`
##### Searching
- `-s`:
- `-f`:
##### Others
- `-ignore [OPTIONS]`:

**NOTE**
1. Multithreaded search is performed using maximum available physical threads on cpu
2. Searching is case-insensitive
3. Common folders such as `.git node_modues` and files with extension `.class .gz` are ignored by default

## Features Implemented
- `CLIMananger` package for parsing out CLI args
- Search space optimization similar to `ripgrep`
- Implement multi threaded file content searching based on work stealing queue
- Single threaded search file content
- Implement fuzzy searching algorithm using Levenshtein distance
- Make `BoundedPriorityQueue` thread safe
- Implement `BoundedPriorityQueue` to store search results
- Create `Serializer` class to cache indexed file tree in a file
- Create custom `ThreadPoolManager`
- Create a `ThreadSafeQueue`
- Multi threaded file indexing
- Multi threaded BFS file searching
- Single threaded DFS file searching
- Single threaded BFS file searching
- Single threaded file indexing