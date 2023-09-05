package es.angelillo15.antiabusers;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class DependencyLoader implements PluginLoader {
    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        RemoteRepository nookure = new RemoteRepository.Builder(
                "nookure",
                "default",
                "https://repo.nookure.com/releases"
        ).build();

        RemoteRepository jitpack = new RemoteRepository.Builder(
                "jitpack",
                "default",
                "https://jitpack.io"
        ).build();

        Dependency core = new Dependency(new DefaultArtifact("es.angelillo15.core:nookcore-core:1.0.6"), null);

        resolver.addRepository(nookure);
        resolver.addRepository(jitpack);
        resolver.addDependency(core);

        classpathBuilder.addLibrary(resolver);
    }
}
